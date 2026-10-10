import { createHash } from "node:crypto";
import { pool } from "../db.js";
import { createSessionToken } from "./password.js";

export function hashSessionToken(token: string): string {
  return createHash("sha256").update(token).digest("hex");
}

export async function createSession(userId: string, deviceId?: string) {
  const token = createSessionToken();
  const tokenHash = hashSessionToken(token);
  const result = await pool.query(
    `insert into auth_sessions(user_id, device_id, token_hash, expires_at)
     values ($1,$2,$3,now()+interval '30 days')
     returning id, expires_at`,
    [userId, deviceId ?? null, tokenHash]
  );
  return { token, sessionId: result.rows[0].id as string, expiresAt: result.rows[0].expires_at as string };
}

export async function revokeSession(token: string) {
  await pool.query(
    `update auth_sessions set revoked_at=now() where token_hash=$1 and revoked_at is null`,
    [hashSessionToken(token)]
  );
}

export async function getAuthenticatedUser(token: string) {
  const result = await pool.query(
    `select s.id as session_id, u.id as user_id, lp.id as learner_id, u.status
       from auth_sessions s
       join users u on u.id=s.user_id
       join learner_profiles lp on lp.user_id=u.id
      where s.token_hash=$1
        and s.revoked_at is null
        and s.expires_at > now()
        and u.status='ACTIVE'`,
    [hashSessionToken(token)]
  );
  return result.rows[0] ?? null;
}
