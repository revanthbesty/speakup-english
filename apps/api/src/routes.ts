import type { IncomingMessage, ServerResponse } from "node:http";
import { register, login, logout } from "./modules/identity/auth-service.js";
import { getAuthenticatedUser } from "./security/session.js";
import { getOnboarding, selectAudience, setGoals, setSelfAssessment, setDailyGoal } from "./modules/onboarding/onboarding-service.js";
import { handleAIRoute } from "./modules/ai/ai-routes.js";
import { handleLearningAIRoute } from "./modules/ai-learning/ai-learning-routes.js";
import { handleSpeechRoute } from "./modules/speech/speech-routes.js";

async function body(req: IncomingMessage) {
  let raw = "";
  for await (const chunk of req) raw += chunk;
  return raw ? JSON.parse(raw) : {};
}

function send(res: ServerResponse, status: number, data: unknown) {
  res.statusCode = status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({ data, meta: { requestId: crypto.randomUUID() } }));
}

function error(res: ServerResponse, status: number, code: string, message: string) {
  res.statusCode = status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({ error: { code, message, requestId: crypto.randomUUID() } }));
}

function token(req: IncomingMessage) {
  const value = req.headers.authorization;
  return value?.startsWith("Bearer ") ? value.slice(7) : null;
}

async function auth(req: IncomingMessage, res: ServerResponse) {
  const t = token(req);
  if (!t) { error(res,401,"UNAUTHENTICATED","Authentication is required."); return null; }
  const user = await getAuthenticatedUser(t);
  if (!user) { error(res,401,"UNAUTHENTICATED","Authentication is required."); return null; }
  return user;
}

export async function handleRoutes(req: IncomingMessage, res: ServerResponse, pathname: string) {
  try {
    if (pathname === "/api/v1/ai/learning") { const handled = await handleLearningAIRoute(req,res,pathname); if (handled !== false) return handled; }
    if (pathname.startsWith("/api/v1/ai/") || pathname.startsWith("/api/v1/admin/ai/")) { const handled = await handleAIRoute(req,res,pathname); if (handled !== false) return handled; }
    if (pathname.startsWith("/api/v1/speech/") || pathname === "/api/v1/admin/ai/speech/providers") { const handled = await handleSpeechRoute(req,res,pathname); if (handled !== false) return handled; }
    if (req.method === "POST" && pathname === "/api/v1/auth/register") {
      const result = await register(await body(req));
      return send(res,201,result);
    }
    if (req.method === "POST" && pathname === "/api/v1/auth/login") {
      return send(res,200,await login(await body(req)));
    }
    if (req.method === "POST" && pathname === "/api/v1/auth/logout") {
      const t = token(req);
      if (t) await logout(t);
      return send(res,204,null);
    }

    if (pathname.startsWith("/api/v1/onboarding")) {
      const current = await auth(req,res);
      if (!current) return;
      if (req.method === "GET" && pathname === "/api/v1/onboarding") {
        return send(res,200,await getOnboarding(current.learner_id));
      }
      if (req.method === "PATCH" && pathname === "/api/v1/onboarding/audience") {
        const input = await body(req); await selectAudience(current.learner_id,input.audience); return send(res,200,{updated:true});
      }
      if (req.method === "PATCH" && pathname === "/api/v1/onboarding/goals") {
        const input = await body(req); await setGoals(current.learner_id,input.goals); return send(res,200,{updated:true});
      }
      if (req.method === "PATCH" && pathname === "/api/v1/onboarding/self-assessment") {
        const input = await body(req); await setSelfAssessment(current.learner_id,input); return send(res,200,{updated:true});
      }
      if (req.method === "PATCH" && pathname === "/api/v1/onboarding/daily-goal") {
        const input = await body(req); await setDailyGoal(current.learner_id,input.minutes); return send(res,200,{updated:true});
      }
    }
    return false;
  } catch (e) {
    const code = e instanceof Error ? e.message : "INTERNAL_ERROR";
    const map: Record<string,[number,string]> = {
      ACCOUNT_ALREADY_EXISTS:[409,"An account with this email already exists."],
      INVALID_CREDENTIALS:[401,"Unable to sign in with those credentials."],
      PASSWORD_TOO_SHORT:[422,"Password must be at least 8 characters."],
      INVALID_EMAIL:[422,"Enter a valid email address."],
      INVALID_AUDIENCE:[422,"Select a supported audience."],
      INVALID_GOAL:[422,"Select supported learning goals."],
      INVALID_DAILY_GOAL:[422,"Select a supported daily learning goal."]
    };
    const [status,message] = map[code] ?? [500,"Something went wrong. Please try again."];
    error(res,status,code,message);
    return true;
  }
}
