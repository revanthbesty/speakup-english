export type ActivityType =
  | "VERB_INTRODUCTION"
  | "SENTENCE_BUILDER"
  | "SPEAKING"
  | "GRAMMAR"
  | "LISTENING"
  | "READING"
  | "WRITING"
  | "VOCABULARY"
  | "PRONUNCIATION"
  | "ROLEPLAY";

export type ActivityState =
  | "CREATED"
  | "READY"
  | "STARTED"
  | "IN_PROGRESS"
  | "SUBMITTED"
  | "EVALUATING"
  | "EVALUATED"
  | "FEEDBACK_SHOWN"
  | "COMPLETED";

export interface ActivityContract {
  id: string;
  version: number;
  type: ActivityType;
  objective: string;
  skills: string[];
  difficulty: {
    linguistic: number;
    vocabulary: number;
    cognitive: number;
    speakingDemand: number;
  };
  instructions: string;
  input: Record<string, unknown>;
  interaction: Record<string, unknown>;
  evaluation: {
    mode: "DETERMINISTIC" | "AI" | "SPEECH" | "HYBRID";
    config: Record<string, unknown>;
  };
  completion: Record<string, unknown>;
  evidence: Array<{skill:string; weight:number}>;
  retry: Record<string, unknown>;
  accessibility: Record<string, unknown>;
  offline: {
    supported: boolean;
    evaluation: "LOCAL" | "SERVER" | "HYBRID";
  };
  analytics: {
    activityStarted: string;
    activityCompleted: string;
  };
}

export interface ActivityRuntimeState {
  activityId: string;
  activityVersion: number;
  attemptId: string;
  state: ActivityState;
  progress: number;
  input: Record<string, unknown>;
  startedAt?: string;
  submittedAt?: string;
}
