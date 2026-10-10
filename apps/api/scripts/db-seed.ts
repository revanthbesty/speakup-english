import fs from "node:fs/promises";
import path from "node:path";
import pg from "pg";
import { config } from "../src/config.js";

const pool = new pg.Pool({ connectionString: config.databaseUrl });
const seed = await fs.readFile(path.resolve("../../database/seed/001_mvp.sql"), "utf8");
await pool.query(seed);
console.log("Deterministic MVP seed applied.");
await pool.end();
