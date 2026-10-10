import http from "node:http";
import { config } from "./config.js";
import { checkDatabase } from "./db.js";
import { MockAIGateway } from "./ai/mock-ai.js";
import type { HealthResponse, ReadinessResponse } from "@speakup/contracts/health";
import { handleRoutes } from "./routes.js";
import { handleDiagnosticRoutes } from "./modules/diagnostic/diagnostic-routes.js";
import { handleJourneyRoutes } from "./modules/journey/journey-routes.js";
import { handleActivityRoutes } from "./modules/activities/activity-routes.js";
import { handleMvpActivityRoutes } from "./modules/activities/mvp-routes.js";
import { handleFeedbackRoutes } from "./modules/activities/feedback-routes.js";
import { handleSpeakingRoutes } from "./modules/speaking/speaking-routes.js";
import { handleEvidenceRoutes } from "./modules/evidence/evidence-routes.js";
import { handleGamificationRoutes } from "./modules/gamification/gamification-routes.js";
import { handleProgressRoutes } from "./modules/progress/progress-routes.js";
import { handleRecommendationRoutes } from "./modules/recommendation/recommendation-routes.js";
import { handleDailyRoutes } from "./modules/daily/daily-routes.js";
import { requestId, applySecurityHeaders } from "./security/http-security.js";

const ai = new MockAIGateway();

function json(res: http.ServerResponse, status: number, body: unknown) {
  res.statusCode = status;
  res.setHeader("content-type", "application/json");
  res.end(JSON.stringify(body));
}

export function createServer() {
  return http.createServer(async (req, res) => {
    const id = requestId(req);
    applySecurityHeaders(res, id);
    const url = new URL(req.url ?? "/", `http://${req.headers.host ?? "localhost"}`);

    if (req.method === "GET" && url.pathname === "/health") {
      const body: HealthResponse = {
        status: "ok",
        service: "speakup-api",
        version: "0.4.0-m5-production",
        timestamp: new Date().toISOString()
      };
      return json(res, 200, body);
    }

    if (req.method === "GET" && url.pathname === "/ready") {
      const dbOk = await checkDatabase();
      const body: ReadinessResponse = {
        status: dbOk ? "ready" : "not_ready",
        checks: { database: dbOk ? "ok" : "failed", ai: "ok" },
        timestamp: new Date().toISOString()
      };
      return json(res, dbOk ? 200 : 503, body);
    }

    const dailyHandled = await handleDailyRoutes(req, res, url.pathname);
    if (dailyHandled) return;

    const recommendationHandled = await handleRecommendationRoutes(req, res, url.pathname);
    if (recommendationHandled) return;

    const progressHandled = await handleProgressRoutes(req, res, url.pathname);
    if (progressHandled) return;

    const gamificationHandled = await handleGamificationRoutes(req, res, url.pathname);
    if (gamificationHandled) return;

    const evidenceHandled = await handleEvidenceRoutes(req, res, url.pathname);
    if (evidenceHandled) return;

    const speakingHandled = await handleSpeakingRoutes(req, res, url.pathname);
    if (speakingHandled) return;

    const feedbackHandled = await handleFeedbackRoutes(req, res, url.pathname);
    if (feedbackHandled) return;

    const mvpActivityHandled = await handleMvpActivityRoutes(req, res, url.pathname);
    if (mvpActivityHandled) return;

    const activityHandled = await handleActivityRoutes(req, res, url.pathname);
    if (activityHandled) return;

    const journeyHandled = await handleJourneyRoutes(req, res, url.pathname);
    if (journeyHandled) return;

    const diagnosticHandled = await handleDiagnosticRoutes(req, res, url.pathname);
    if (diagnosticHandled) return;

    const handled = await handleRoutes(req, res, url.pathname);
    if (handled) return;

    if (req.method === "GET" && url.pathname === "/api/v1/dev/mock-ai/evaluate") {
      const result = await ai.evaluate({
        activityId: "demo-activity",
        attemptId: "demo-attempt",
        learnerInput: "I eat rice for breakfast.",
        scenario: (url.searchParams.get("scenario") as any) ?? "CORRECT"
      });
      return json(res, 200, { data: result, meta: { requestId: id } });
    }

    return json(res, 404, {
      error: {
        code: "NOT_FOUND",
        message: "Route not found",
        requestId: id
      }
    });
  });
}

export function start() {
  const server = createServer();
  server.listen(config.port, () => {
    console.log(`SpeakUp API listening on http://localhost:${config.port}`);
  });
  const shutdown = () => server.close(() => process.exit(0));
  process.on("SIGINT", shutdown);
  process.on("SIGTERM", shutdown);
  return server;
}
