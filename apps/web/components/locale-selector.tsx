"use client";
import { useAccessibility } from "./accessibility-provider";
export function LocaleSelector() { const a=useAccessibility(); return <label className="locale-select"><span className="sr-only">{a.t("language")}</span><select aria-label={a.t("language")} value={a.locale} onChange={e=>a.setLocale(e.target.value as typeof a.locale)}><option value="en">English</option><option value="hi">हिन्दी</option><option value="ta">தமிழ்</option><option value="ar">العربية</option></select></label>; }
