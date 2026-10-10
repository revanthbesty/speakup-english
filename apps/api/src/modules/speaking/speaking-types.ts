export type MicPermission =
  | "UNKNOWN"
  | "REQUESTING"
  | "GRANTED"
  | "DENIED"
  | "BLOCKED"
  | "UNAVAILABLE";

export type AudioState =
  | "IDLE"
  | "PREPARING"
  | "RECORDING"
  | "STOPPING"
  | "UPLOADING"
  | "PROCESSING"
  | "COMPLETE"
  | "FAILED";

export type SpeechProcessingStatus =
  | "READY"
  | "CAPTURING"
  | "UPLOADING"
  | "PROCESSING"
  | "COMPLETED"
  | "FAILED"
  | "CANCELLED";

export interface SpeakingPrompt {
  activityId: string;
  prompt: string;
  objective: string;
  minimumDurationMs?: number;
  maximumDurationMs: number;
  retainRawAudio: false;
}

export interface SttResult {
  transcript: string;
  confidence: number;
  language: string;
  durationMs?: number;
}

export interface SpeechProvider {
  transcribe(input: {
    temporaryObjectKey: string;
    language: string;
  }): Promise<SttResult>;
}
