import pg from "pg";
import { config } from "./config.js";
const { Pool } = pg;

export const pool = new Pool({
  connectionString: config.databaseUrl,
  max: 10,
  idleTimeoutMillis: 30_000
});

export async function checkDatabase(): Promise<boolean> {
  try {
    await pool.query("select 1");
    return true;
  } catch {
    return false;
  }
}
