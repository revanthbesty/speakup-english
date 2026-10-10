import { pool } from "../../db.js";

const SECTIONS = [
  "VOCABULARY",
  "GRAMMAR",
  "LISTENING",
  "READING",
  "SENTENCE_CONSTRUCTION",
  "SPEAKING"
] as const;

type SectionCode = typeof SECTIONS[number];

const proficiencyOrder = ["P0","P1","P2","P3"] as const;

function scoreToProficiency(score: number) {
  if (score < 0.25) return "P0";
  if (score < 0.5) return "P1";
  if (score < 0.75) return "P2";
  return "P3";
}

export async function createDiagnostic(learnerId: string) {
  const existing = await pool.query(
    `select id,status,version from diagnostics
      where learner_id=$1 and status in ('CREATED','IN_PROGRESS')
      order by created_at desc limit 1`,
    [learnerId]
  );
  if (existing.rowCount) return getDiagnostic(learnerId, existing.rows[0].id);

  const client = await pool.connect();
  try {
    await client.query("begin");
    const d = await client.query(
      `insert into diagnostics(learner_id,version,status) values($1,1,'CREATED') returning id`,
      [learnerId]
    );
    const diagnosticId = d.rows[0].id as string;
    for (let i = 0; i < SECTIONS.length; i++) {
      await client.query(
        `insert into diagnostic_sections(diagnostic_id,section_code,sequence_no)
         values($1,$2,$3)`,
        [diagnosticId, SECTIONS[i], i + 1]
      );
    }
    await client.query(
      `update onboarding_states set state='DIAGNOSTIC_READY',updated_at=now()
        where learner_id=$1`,
      [learnerId]
    );
    await client.query("commit");
    return getDiagnostic(learnerId, diagnosticId);
  } catch (e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }
}

export async function getDiagnostic(learnerId: string, diagnosticId: string) {
  const d = await pool.query(
    `select d.id,d.version,d.status,d.recommended_proficiency,
            d.recommendation_confidence,d.started_at,d.completed_at
       from diagnostics d
      where d.id=$1 and d.learner_id=$2`,
    [diagnosticId, learnerId]
  );
  if (!d.rowCount) throw new Error("DIAGNOSTIC_NOT_FOUND");

  const sections = await pool.query(
    `select id,section_code,sequence_no,version,status
       from diagnostic_sections
      where diagnostic_id=$1 order by sequence_no`,
    [diagnosticId]
  );
  return { ...d.rows[0], sections: sections.rows };
}

export async function startDiagnostic(learnerId: string, diagnosticId: string) {
  await assertOwnership(learnerId, diagnosticId);
  await pool.query(
    `update diagnostics set status='IN_PROGRESS',started_at=coalesce(started_at,now()),updated_at=now()
      where id=$1`,
    [diagnosticId]
  );
  await pool.query(
    `update onboarding_states set state='DIAGNOSTIC_IN_PROGRESS',updated_at=now()
      where learner_id=$1`,
    [learnerId]
  );
  return getDiagnostic(learnerId, diagnosticId);
}

export async function submitAttempt(
  learnerId: string,
  diagnosticId: string,
  sectionCode: SectionCode,
  input: Record<string, unknown>
) {
  await assertOwnership(learnerId, diagnosticId);
  if (!SECTIONS.includes(sectionCode)) throw new Error("INVALID_DIAGNOSTIC_SECTION");

  const section = await pool.query(
    `select id from diagnostic_sections
      where diagnostic_id=$1 and section_code=$2`,
    [diagnosticId, sectionCode]
  );
  if (!section.rowCount) throw new Error("DIAGNOSTIC_SECTION_NOT_FOUND");

  const sectionId = section.rows[0].id as string;
  const next = await pool.query(
    `select coalesce(max(attempt_no),0)+1 as attempt_no
       from diagnostic_attempts where diagnostic_section_id=$1`,
    [sectionId]
  );
  const attemptNo = Number(next.rows[0].attempt_no);

  // Foundation evaluator: deterministic only. Speaking can later use the AI/speech gateway.
  const score = typeof input.score === "number"
    ? Math.max(0, Math.min(1, input.score))
    : 0.5;

  const proficiency = scoreToProficiency(score);
  const confidence = typeof input.confidence === "number"
    ? Math.max(0, Math.min(1, input.confidence))
    : 0.7;

  const result = await pool.query(
    `insert into diagnostic_attempts
       (diagnostic_section_id,attempt_no,input,outcome,score,confidence,evaluation)
     values($1,$2,$3,'EVALUATED',$4,$5,$6)
     returning id,attempt_no,score,confidence`,
    [
      sectionId, attemptNo, input, score, confidence,
      { proficiency, evaluator: "foundation-deterministic-v1" }
    ]
  );

  await pool.query(
    `update diagnostic_sections set status='COMPLETED' where id=$1`,
    [sectionId]
  );

  return result.rows[0];
}

export async function completeDiagnostic(learnerId: string, diagnosticId: string) {
  await assertOwnership(learnerId, diagnosticId);
  const rows = await pool.query(
    `select ds.section_code, da.score, da.confidence
       from diagnostic_sections ds
       left join lateral (
         select * from diagnostic_attempts x
          where x.diagnostic_section_id=ds.id
          order by attempt_no desc limit 1
       ) da on true
      where ds.diagnostic_id=$1
      order by ds.sequence_no`,
    [diagnosticId]
  );

  if (rows.rows.some(r => r.score === null)) throw new Error("DIAGNOSTIC_INCOMPLETE");

  const sectionResults = rows.rows.map(r => ({
    section: r.section_code,
    score: Number(r.score),
    confidence: Number(r.confidence)
  }));

  const average = sectionResults.reduce((s,r) => s + r.score,0) / sectionResults.length;
  const recommended = scoreToProficiency(average);
  const confidence = sectionResults.reduce((s,r) => s + r.confidence,0) / sectionResults.length;

  const skillProfile = Object.fromEntries(
    sectionResults.map(r => [r.section.toLowerCase(), {
      score: r.score, confidence: r.confidence
    }])
  );

  const startingPoint = {
    proficiency: recommended,
    focusSkills: sectionResults
      .filter(r => r.score < average)
      .map(r => r.section),
    support: average < 0.5 ? "HIGH" : average < 0.75 ? "MEDIUM" : "LOW"
  };

  const client = await pool.connect();
  try {
    await client.query("begin");
    await client.query(
      `insert into diagnostic_results
       (diagnostic_id,section_results,skill_profile,recommended_proficiency,confidence,rationale)
       values($1,$2,$3,$4,$5,$6)
       on conflict(diagnostic_id) do update set
         section_results=excluded.section_results,
         skill_profile=excluded.skill_profile,
         recommended_proficiency=excluded.recommended_proficiency,
         confidence=excluded.confidence,
         rationale=excluded.rationale`,
      [diagnosticId, sectionResults, skillProfile, recommended, confidence, {
        method: "equal-weight-foundation-v1",
        startingPoint
      }]
    );

    await client.query(
      `update diagnostics set status='COMPLETED',
         recommended_proficiency=$2,recommendation_confidence=$3,
         completed_at=now(),updated_at=now()
       where id=$1`,
      [diagnosticId, recommended, confidence]
    );

    await client.query(
      `insert into placement_decisions
       (learner_id,diagnostic_id,recommended_proficiency,recommended_starting_point)
       values($1,$2,$3,$4)
       on conflict do nothing`,
      [learnerId, diagnosticId, recommended, startingPoint]
    );

    await client.query(
      `update onboarding_states set state='PLACEMENT_READY',updated_at=now()
        where learner_id=$1`,
      [learnerId]
    );
    await client.query("commit");
  } catch(e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }

  return getDiagnosticResult(learnerId, diagnosticId);
}

export async function getDiagnosticResult(learnerId: string, diagnosticId: string) {
  await assertOwnership(learnerId, diagnosticId);
  const result = await pool.query(
    `select d.id,d.status,d.recommended_proficiency,d.recommendation_confidence,
            dr.section_results,dr.skill_profile,dr.rationale,
            pd.id placement_id,pd.recommended_starting_point,
            pd.learner_selected_proficiency,pd.learner_selected_starting_point,
            pd.decision_type,pd.decided_at
       from diagnostics d
       join diagnostic_results dr on dr.diagnostic_id=d.id
       left join placement_decisions pd on pd.diagnostic_id=d.id
      where d.id=$1`,
    [diagnosticId]
  );
  if (!result.rowCount) throw new Error("DIAGNOSTIC_RESULT_NOT_FOUND");
  return result.rows[0];
}

export async function decidePlacement(
  learnerId: string,
  diagnosticId: string,
  decision: { type: "ACCEPTED"|"OVERRIDDEN"; proficiency: string; startingPoint?: Record<string,unknown> }
) {
  await assertOwnership(learnerId, diagnosticId);
  if (!proficiencyOrder.includes(decision.proficiency as any)) throw new Error("INVALID_PROFICIENCY");

  const result = await pool.query(
    `select recommended_proficiency,recommended_starting_point
       from placement_decisions
      where learner_id=$1 and diagnostic_id=$2`,
    [learnerId, diagnosticId]
  );
  if (!result.rowCount) throw new Error("PLACEMENT_NOT_FOUND");

  const recommended = result.rows[0];
  const startingPoint = decision.startingPoint ?? recommended.recommended_starting_point;

  await pool.query(
    `update placement_decisions
        set learner_selected_proficiency=$3,
            learner_selected_starting_point=$4,
            decision_type=$5,
            decided_at=now()
      where learner_id=$1 and diagnostic_id=$2`,
    [learnerId, diagnosticId, decision.proficiency, startingPoint, decision.type]
  );

  await pool.query(
    `update learner_profiles set proficiency=$2,updated_at=now() where id=$1`,
    [learnerId, decision.proficiency]
  );

  await pool.query(
    `update onboarding_states set state='PLACEMENT_CONFIRMED',updated_at=now()
      where learner_id=$1`,
    [learnerId]
  );

  return {
    decision: decision.type,
    proficiency: decision.proficiency,
    startingPoint
  };
}

async function assertOwnership(learnerId: string, diagnosticId: string) {
  const result = await pool.query(
    `select 1 from diagnostics where id=$1 and learner_id=$2`,
    [diagnosticId, learnerId]
  );
  if (!result.rowCount) throw new Error("DIAGNOSTIC_NOT_FOUND");
}
