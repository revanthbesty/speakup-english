export interface HealthResponse {
  status: "ok";
  service: string;
  version: string;
  timestamp: string;
}
export interface ReadinessResponse {
  status: "ready" | "not_ready";
  checks: Record<string, "ok" | "failed">;
  timestamp: string;
}
