"use client";
import Link from "next/link";
import { useAccessibility } from "./accessibility-provider";
export function NavLabels({ meOnly=false }: { meOnly?: boolean }) { const a=useAccessibility(); if (meOnly) return <>{a.t("me")}</>; return <><Link href="/home">{a.t("home")}</Link><Link href="/journey">{a.t("journey")}</Link><Link href="/games/real-world-quests">Games</Link><Link href="/progress">{a.t("progress")}</Link></>; }
