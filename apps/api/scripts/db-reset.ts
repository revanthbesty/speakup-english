import pg from "pg";
import { config } from "../src/config.js";
const pool = new pg.Pool({ connectionString: config.databaseUrl });
await pool.query("drop schema public cascade; create schema public;");
await pool.end();
console.log("Database reset. Run db:migrate then db:seed.");
