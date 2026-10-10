import { randomBytes, scrypt as nodeScrypt, timingSafeEqual } from "node:crypto";
import { promisify } from "node:util";

const scrypt = promisify(nodeScrypt);
const KEY_LENGTH = 64;

export async function hashPassword(password: string): Promise<string> {
  if (password.length < 8) throw new Error("PASSWORD_TOO_SHORT");
  const salt = randomBytes(16);
  const derived = (await scrypt(password, salt, KEY_LENGTH)) as Buffer;
  return `scrypt$${salt.toString("base64url")}$${derived.toString("base64url")}`;
}

export async function verifyPassword(password: string, encoded: string): Promise<boolean> {
  const [scheme, saltText, hashText] = encoded.split("$");
  if (scheme !== "scrypt" || !saltText || !hashText) return false;
  const salt = Buffer.from(saltText, "base64url");
  const expected = Buffer.from(hashText, "base64url");
  const actual = (await scrypt(password, salt, expected.length)) as Buffer;
  return actual.length === expected.length && timingSafeEqual(actual, expected);
}

export function createSessionToken(): string {
  return randomBytes(32).toString("base64url");
}
