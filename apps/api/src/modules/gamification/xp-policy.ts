export const XP_POLICY_VERSION = "mvp-xp-v1";

export type XpOutcome = "CORRECT"|"PARTIAL"|"INCORRECT"|"NEUTRAL"|"LOW_CONFIDENCE";

export function calculateXp(input:{
  outcome:XpOutcome;
  confidence:number;
  evidenceStrength:number;
  isRetry:boolean;
}):number{
  if(input.outcome==="NEUTRAL" || input.outcome==="LOW_CONFIDENCE") return 0;
  if(input.confidence<0.55 || input.evidenceStrength<=0) return 0;

  const base=input.outcome==="CORRECT"?20:
    input.outcome==="PARTIAL"?12:4;
  const retryReduction=input.isRetry?0.5:1;
  const confidenceFactor=Math.max(.5,Math.min(1,input.confidence));
  const strengthFactor=Math.max(.5,Math.min(1,input.evidenceStrength));

  return Math.max(0,Math.floor(base*retryReduction*confidenceFactor*strengthFactor));
}

export function levelForXp(totalXp:number):number{
  if(totalXp<50) return 1;
  if(totalXp<120) return 2;
  if(totalXp<250) return 3;
  if(totalXp<450) return 4;
  if(totalXp<700) return 5;
  return 6 + Math.floor((totalXp-700)/300);
}
