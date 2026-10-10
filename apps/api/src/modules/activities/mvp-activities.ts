import { pool } from "../../db.js";

type SentenceEvaluation = {
  outcome: "CORRECT" | "PARTIAL" | "INCORRECT";
  score: number;
  confidence: number;
  feedback: {
    headline: string;
    explanation: string;
    retryHint?: string;
  };
  evidence: Array<{skill:string; weight:number}>;
};

const canonical = ["I","eat","rice","for","breakfast"];

function normalizeWords(value: unknown): string[] {
  if (Array.isArray(value)) {
    return value.map(String).map(x => x.trim().toLowerCase()).filter(Boolean);
  }
  return String(value ?? "")
    .trim()
    .replace(/[.,!?]/g,"")
    .split(/\s+/)
    .filter(Boolean)
    .map(x => x.toLowerCase());
}

export function evaluateSentence(input: unknown): SentenceEvaluation {
  const words = normalizeWords(input);
  const expected = canonical.map(x=>x.toLowerCase());

  if (!words.length) {
    return {
      outcome:"INCORRECT",score:0,confidence:1,
      feedback:{
        headline:"Let's build it together.",
        explanation:"Start with the person: I.",
        retryHint:"Try: I → eat → rice → for → breakfast."
      },
      evidence:[{skill:"SENTENCE_CONSTRUCTION",weight:1}]
    };
  }

  const matches = words.filter((word,i)=>word===expected[i]).length;
  const score = matches / Math.max(words.length,expected.length);

  if (words.join(" ") === expected.join(" ")) {
    return {
      outcome:"CORRECT",score:1,confidence:1,
      feedback:{
        headline:"Great sentence!",
        explanation:"You built the target sentence correctly."
      },
      evidence:[
        {skill:"SENTENCE_CONSTRUCTION",weight:1},
        {skill:"GRAMMAR",weight:.5},
        {skill:"VOCABULARY",weight:.5}
      ]
    };
  }

  if (score >= .4 && words.every(word => expected.includes(word))) {
    const missing = expected.filter((x,i)=>words[i]!==x);
    return {
      outcome:"PARTIAL",score,confidence:.95,
      feedback:{
        headline:"Good attempt. Let's improve one part.",
        explanation:`Check the order around: ${missing.slice(0,2).join(" and ")}.`,
        retryHint:"Use the sentence pattern: WHO → ACTION → WHAT → WHEN."
      },
      evidence:[{skill:"SENTENCE_CONSTRUCTION",weight:.75}]
    };
  }

  return {
    outcome:"INCORRECT",score,confidence:.95,
    feedback:{
      headline:"Good try. Let's build it again.",
      explanation:"The sentence needs a different word order.",
      retryHint:"Start with I, then the action eat."
    },
    evidence:[{skill:"SENTENCE_CONSTRUCTION",weight:.5}]
  };
}

export function getMvpActivityDefinition(type:string) {
  if(type==="VERB_INTRODUCTION") {
    return {
      type,
      objective:"Understand and use the action word eat.",
      targetVerb:"eat",
      meaning:"to put food in your mouth and have it as food",
      examples:["I eat rice.","I eat breakfast.","I eat rice for breakfast."],
      interaction:{kind:"meaning_and_choice",options:["eat","sleep","run"]},
      evaluation:{mode:"DETERMINISTIC"}
    };
  }
  if(type==="SENTENCE_BUILDER") {
    return {
      type,
      objective:"Build a practical sentence using the target action.",
      targetSentence:"I eat rice for breakfast.",
      pattern:["WHO","ACTION","WHAT","WHEN"],
      tokens:canonical,
      interaction:{kind:"word_arrangement",reorderable:true},
      evaluation:{mode:"DETERMINISTIC"}
    };
  }
  throw new Error("UNSUPPORTED_MVP_ACTIVITY");
}

export async function recordDeterministicEvaluation(
  learnerId:string,
  attemptId:string,
  evaluation:SentenceEvaluation
) {
  const attempt = await pool.query(
    `select aa.id,aa.activity_id,aa.status
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id
      where aa.id=$1 and ls.learner_id=$2`,
    [attemptId,learnerId]
  );
  if(!attempt.rowCount) throw new Error("ATTEMPT_NOT_FOUND");

  const client=await pool.connect();
  try {
    await client.query("begin");

    const evaluationResult=await client.query(
      `insert into evaluations
       (attempt_id,evaluation_mode,status,outcome,score,confidence,result,evaluator_version)
       values($1,'DETERMINISTIC','COMPLETED',$2,$3,$4,$5,'mvp-deterministic-v1')
       returning id`,
      [
        attemptId,
        evaluation.outcome,
        evaluation.score,
        evaluation.confidence,
        JSON.stringify({feedback:evaluation.feedback,evidence:evaluation.evidence})
      ]
    );

    await client.query(
      `update activity_attempts
          set status='EVALUATED'
        where id=$1`,
      [attemptId]
    );

    await client.query("commit");
    return {evaluationId:evaluationResult.rows[0].id,...evaluation};
  } catch(e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }
}
