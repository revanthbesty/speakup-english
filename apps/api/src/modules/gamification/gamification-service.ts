import {pool} from "../../db.js";
import {calculateXp,levelForXp,XP_POLICY_VERSION} from "./xp-policy.js";

export async function awardXpFromEvidence(learnerId:string,evidenceId:string){
  const source=await pool.query(
    `select le.id,le.skill,le.evidence_strength,le.confidence,
            e.outcome,aa.id attempt_id,aa.activity_id,
            exists(
              select 1 from activity_attempts prior
              join learning_sessions pls on pls.id=prior.session_id
              join learning_sessions cls on cls.id=aa.session_id
              where pls.learner_id=$1
                and prior.activity_id=aa.activity_id
                and prior.created_at < aa.created_at
            ) as is_retry
       from learning_evidence le
       join evaluations e on e.id=le.evaluation_id
       join activity_attempts aa on aa.id=le.attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where le.id=$2 and ls.learner_id=$1`,
    [learnerId,evidenceId]
  );
  if(!source.rowCount) throw new Error("EVIDENCE_NOT_FOUND");

  const row=source.rows[0];
  const xp=calculateXp({
    outcome:row.outcome,
    confidence:Number(row.confidence),
    evidenceStrength:Number(row.evidence_strength),
    isRetry:Boolean(row.is_retry)
  });

  const client=await pool.connect();
  try{
    await client.query("begin");
    // Serialize XP mutation per learner so the anti-farming cap is race-safe.
    await client.query(`select pg_advisory_xact_lock(hashtext($1))`,[learnerId]);

    const eventKey=`EVIDENCE:${evidenceId}`;
    const dailyActivityXp=await client.query(
      `select coalesce(sum(xp),0) total
         from xp_events
        where learner_id=$1
          and created_at >= date_trunc('day',now())
          and metadata->>'activityId'=$2`,
      [learnerId,String(row.activity_id)]
    );
    const remainingDailyActivityCap=Math.max(0,40-Number(dailyActivityXp.rows[0].total));
    const cappedXp=Math.min(xp,remainingDailyActivityCap);

    const inserted=await client.query(
      `insert into xp_events
       (learner_id,source_type,source_id,xp,policy_version,event_key,reason,metadata)
       values($1,'LEARNING_EVIDENCE',$2,$3,$4,$5,$6,$7)
       on conflict(learner_id,event_key) do nothing
       returning id,xp,event_key`,
      [
        learnerId,evidenceId,cappedXp,XP_POLICY_VERSION,eventKey,
        cappedXp>0?"MEANINGFUL_LEARNING":"NO_XP_LOW_SIGNAL",
        JSON.stringify({skill:row.skill,outcome:row.outcome,isRetry:Boolean(row.is_retry),activityId:row.activity_id,dailyActivityCap:40})
      ]
    );

    const prior=await client.query(
      `select total_xp,level from learner_gamification where learner_id=$1 for update`,
      [learnerId]
    );
    const oldXp=prior.rowCount?Number(prior.rows[0].total_xp):0;
    const oldLevel=prior.rowCount?Number(prior.rows[0].level):1;
    const appliedXp=inserted.rowCount?Number(inserted.rows[0].xp):0;
    const newXp=oldXp+appliedXp;
    const newLevel=levelForXp(newXp);

    await client.query(
      `insert into learner_gamification(learner_id,total_xp,level)
       values($1,$2,$3)
       on conflict(learner_id) do update set
         total_xp=excluded.total_xp,
         level=excluded.level,
         updated_at=now()`,
      [learnerId,newXp,newLevel]
    );

    const rewards=await client.query(
      `insert into learner_rewards(learner_id,reward_id)
       select $1,rd.id
         from reward_definitions rd
        where rd.status='ACTIVE'
          and rd.unlock_level <= $2
          and rd.xp_threshold <= $3
       on conflict(learner_id,reward_id) do nothing
       returning reward_id`,
      [learnerId,newLevel,newXp]
    );

    await client.query("commit");
    return {
      awardedXp:appliedXp,
      totalXp:newXp,
      previousLevel:oldLevel,
      level:newLevel,
      levelUp:newLevel>oldLevel,
      newlyUnlockedRewardIds:rewards.rows.map(r=>r.reward_id),
      idempotent:!inserted.rowCount
    };
  }catch(error){
    await client.query("rollback");
    throw error;
  }finally{
    client.release();
  }
}

export async function getGamification(learnerId:string){
  const summary=await pool.query(
    `select coalesce(lg.total_xp,0) total_xp,
            coalesce(lg.level,1) level,
            coalesce((select count(*) from learner_rewards lr where lr.learner_id=$1),0) reward_count
       from (select $1::uuid learner_id) seed
       left join learner_gamification lg on lg.learner_id=seed.learner_id`,
    [learnerId]
  );

  const rewards=await pool.query(
    `select rd.code,rd.name,rd.description,rd.unlock_level,rd.xp_threshold,lr.unlocked_at
       from learner_rewards lr
       join reward_definitions rd on rd.id=lr.reward_id
      where lr.learner_id=$1
      order by lr.unlocked_at desc`,
    [learnerId]
  );

  return {summary:summary.rows[0],rewards:rewards.rows};
}

export async function getXpHistory(learnerId:string,limit=25){
  const safeLimit=Math.max(1,Math.min(100,Math.floor(limit)));
  const result=await pool.query(
    `select id,source_type,source_id,xp,reason,policy_version,created_at
       from xp_events where learner_id=$1
      order by created_at desc limit $2`,
    [learnerId,safeLimit]
  );
  return result.rows;
}
