import fs from "node:fs/promises";
import path from "node:path";
import pg from "pg";
import { config } from "../src/config.js";

const pool = new pg.Pool({ connectionString: config.databaseUrl });
await pool.query(`create table if not exists schema_migrations (
  version varchar(100) primary key,
  applied_at timestamptz not null default now()
)`);
const dir = path.resolve("../../database/migrations");
const files = (await fs.readdir(dir)).filter(f => f.endsWith(".sql")).sort();

for (const file of files) {
  const version = file.split("_")[0];
  const exists = await pool.query("select 1 from schema_migrations where version=$1", [version]);
  if (exists.rowCount) continue;
  const sql = await fs.readFile(path.join(dir, file), "utf8");
  const client = await pool.connect();
  try {
    await client.query("begin");
    await client.query(sql);
    await client.query("insert into schema_migrations(version) values($1)", [version]);
    await client.query("commit");
    console.log(`Applied migration ${file}`);
  } catch (e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }
}
await pool.end();
