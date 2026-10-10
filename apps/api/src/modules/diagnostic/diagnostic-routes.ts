import type { IncomingMessage, ServerResponse } from "node:http";
import { getAuthenticatedUser } from "../../security/session.js";
import {
  createDiagnostic, getDiagnostic, startDiagnostic, submitAttempt,
  completeDiagnostic, getDiagnosticResult, decidePlacement
} from "./diagnostic-service.js";

async function readBody(req: IncomingMessage) {
  let raw = "";
  for await (const chunk of req) raw += chunk;
  return raw ? JSON.parse(raw) : {};
}

function token(req: IncomingMessage) {
  const v = req.headers.authorization;
  return v?.startsWith("Bearer ") ? v.slice(7) : null;
}

function send(res: ServerResponse, status: number, data: unknown) {
  res.statusCode = status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({ data, meta:{requestId:crypto.randomUUID()} }));
}

function fail(res: ServerResponse,status:number,code:string,message:string) {
  res.statusCode=status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}

export async function handleDiagnosticRoutes(
  req: IncomingMessage, res: ServerResponse, pathname: string
) {
  if (!pathname.startsWith("/api/v1/diagnostics")) return false;
  const sessionToken = token(req);
  if (!sessionToken) { fail(res,401,"UNAUTHENTICATED","Authentication is required."); return true; }
  const user = await getAuthenticatedUser(sessionToken);
  if (!user) { fail(res,401,"UNAUTHENTICATED","Authentication is required."); return true; }

  try {
    if (req.method==="POST" && pathname==="/api/v1/diagnostics") {
      return send(res,201,await createDiagnostic(user.learner_id));
    }

    const match = pathname.match(/^\/api\/v1\/diagnostics\/([^/]+)(?:\/(.+))?$/);
    if (!match) return fail(res,404,"NOT_FOUND","Diagnostic route not found.");

    const diagnosticId = match[1]!;
    const action = match[2]!;

    if (req.method==="GET" && !action) return send(res,200,await getDiagnostic(user.learner_id,diagnosticId));
    if (req.method==="POST" && action==="start") return send(res,200,await startDiagnostic(user.learner_id,diagnosticId));
    if (req.method==="POST" && action==="attempts") {
      const input = await readBody(req);
      return send(res,201,await submitAttempt(user.learner_id,diagnosticId,input.sectionCode,input.input ?? {}));
    }
    if (req.method==="POST" && action==="complete") return send(res,200,await completeDiagnostic(user.learner_id,diagnosticId));
    if (req.method==="GET" && action==="result") return send(res,200,await getDiagnosticResult(user.learner_id,diagnosticId));
    if (req.method==="POST" && action==="placement") {
      return send(res,200,await decidePlacement(user.learner_id,diagnosticId,await readBody(req)));
    }

    return fail(res,404,"NOT_FOUND","Diagnostic action not found.");
  } catch (e) {
    const code=e instanceof Error?e.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      DIAGNOSTIC_NOT_FOUND:[404,"Diagnostic not found."],
      DIAGNOSTIC_INCOMPLETE:[422,"Complete every diagnostic section before finishing."],
      INVALID_DIAGNOSTIC_SECTION:[422,"Unsupported diagnostic section."],
      INVALID_PROFICIENCY:[422,"Unsupported proficiency level."],
      DIAGNOSTIC_RESULT_NOT_FOUND:[404,"Diagnostic result is not available yet."],
      PLACEMENT_NOT_FOUND:[404,"Placement decision is not available."]
    };
    const [status,message]=known[code]??[500,"Unable to process the diagnostic request."];
    fail(res,status,code,message);
    return true;
  }
}
