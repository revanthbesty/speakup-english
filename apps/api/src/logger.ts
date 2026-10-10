export function log(level: "info" | "warn" | "error", message: string, data: Record<string, unknown> = {}) {
  console.log(JSON.stringify({
    timestamp: new Date().toISOString(),
    level,
    service: "speakup-api",
    message,
    ...data
  }));
}
