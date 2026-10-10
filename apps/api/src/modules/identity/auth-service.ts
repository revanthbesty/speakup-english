import { pool } from "../../db.js";
import { hashPassword, verifyPassword } from "../../security/password.js";
import { createSession, revokeSession } from "../../security/session.js";

export async function register(input: {
  email: string; password: string; displayName?: string;
}) {
  const email = input.email.trim().toLowerCase();
  if (!email.includes("@")) throw new Error("INVALID_EMAIL");

  const client = await pool.connect();
  try {
    await client.query("begin");
    const existing = await client.query("select id from users where email=$1", [email]);
    if (existing.rowCount) throw new Error("ACCOUNT_ALREADY_EXISTS");

    const passwordHash = await hashPassword(input.password);
    const user = await client.query(
      `insert into users(email,password_hash,status)
       values($1,$2,'ACTIVE') returning id`,
      [email, passwordHash]
    );
    const userId = user.rows[0].id as string;

    const learner = await client.query(
      `insert into learner_profiles(user_id,display_name)
       values($1,$2) returning id`,
      [userId, input.displayName?.trim() || null]
    );

    await client.query(
      `insert into learner_preferences(learner_id) values($1)`,
      [learner.rows[0].id]
    );
    await client.query(
      `insert into onboarding_states(learner_id) values($1)`,
      [learner.rows[0].id]
    );
    await client.query("commit");

    const session = await createSession(userId);
    return { userId, learnerId: learner.rows[0].id as string, ...session };
  } catch (error) {
    await client.query("rollback");
    throw error;
  } finally {
    client.release();
  }
}

export async function login(input: { email: string; password: string }) {
  const result = await pool.query(
    `select u.id user_id, u.password_hash, lp.id learner_id
       from users u join learner_profiles lp on lp.user_id=u.id
      where u.email=$1 and u.status='ACTIVE'`,
    [input.email.trim().toLowerCase()]
  );
  const row = result.rows[0];
  if (!row?.password_hash || !(await verifyPassword(input.password, row.password_hash))) {
    throw new Error("INVALID_CREDENTIALS");
  }
  return { userId: row.user_id as string, learnerId: row.learner_id as string, ...(await createSession(row.user_id)) };
}

export async function logout(token: string) {
  await revokeSession(token);
}
