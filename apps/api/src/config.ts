import "dotenv/config";

function required(name: string): string {
  const value = process.env[name];
  if (!value) throw new Error(`Missing required configuration: ${name}`);
  return value;
}

export const config = {
  nodeEnv: process.env.NODE_ENV ?? "development",
  port: Number(process.env.PORT ?? 4000),
  databaseUrl: required("DATABASE_URL"),
  aiMode: required("AI_MODE", "mock"),
  aiProviderKey: process.env.AI_PROVIDER_KEY ?? "external",
  aiProviderEndpoint: process.env.AI_PROVIDER_ENDPOINT ?? "",
  aiProviderApiKey: process.env.AI_PROVIDER_API_KEY ?? "",
  aiModel: process.env.AI_MODEL ?? "production-default",
  aiTimeoutMs: Number(process.env.AI_TIMEOUT_MS ?? 8000),
  aiFailureThreshold: Number(process.env.AI_FAILURE_THRESHOLD ?? 3),
  aiCircuitCooldownMs: Number(process.env.AI_CIRCUIT_COOLDOWN_MS ?? 30000),
  logLevel: process.env.LOG_LEVEL ?? "info"
} as const;
