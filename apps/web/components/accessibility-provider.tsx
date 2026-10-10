"use client";
import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import type { AccessibilityPreferences } from "@speakup/accessibility";
import { DEFAULT_ACCESSIBILITY } from "@speakup/accessibility";
import { isRtl, type SupportedLocale, translate, type MessageKey } from "@speakup/localization";

type ContextValue = AccessibilityPreferences & { locale: SupportedLocale; setLocale: (locale: SupportedLocale) => void; setPreference: <K extends keyof AccessibilityPreferences>(key: K, value: AccessibilityPreferences[K]) => void; t: (key: MessageKey) => string; };
const Ctx = createContext<ContextValue | null>(null);

export function AccessibilityProvider({ children }: { children: ReactNode }) {
  const [prefs, setPrefs] = useState<AccessibilityPreferences>(DEFAULT_ACCESSIBILITY);
  const [locale, setLocaleState] = useState<SupportedLocale>("en");
  useEffect(() => {
    try {
      const saved = JSON.parse(localStorage.getItem("speakup-accessibility") || "null");
      if (saved) setPrefs({ ...DEFAULT_ACCESSIBILITY, ...saved });
      const savedLocale = localStorage.getItem("speakup-locale") as SupportedLocale | null;
      if (savedLocale) setLocaleState(savedLocale);
    } catch { /* keep safe defaults */ }
  }, []);
  useEffect(() => { document.documentElement.lang = locale; document.documentElement.dir = isRtl(locale) ? "rtl" : "ltr"; document.documentElement.classList.toggle("text-large", prefs.textScale === "large"); document.documentElement.classList.toggle("text-xlarge", prefs.textScale === "xlarge"); document.documentElement.classList.toggle("reduce-motion", prefs.reducedMotion); document.documentElement.classList.toggle("high-contrast", prefs.highContrast); }, [locale, prefs]);
  useEffect(() => { localStorage.setItem("speakup-accessibility", JSON.stringify(prefs)); }, [prefs]);
  const value = useMemo(() => ({ ...prefs, locale, setLocale: (next: SupportedLocale) => { setLocaleState(next); localStorage.setItem("speakup-locale", next); }, setPreference: <K extends keyof AccessibilityPreferences>(key: K, value: AccessibilityPreferences[K]) => setPrefs(p => ({ ...p, [key]: value })), t: (key: MessageKey) => translate(locale, key) }), [prefs, locale]);
  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}
export function useAccessibility() { const value = useContext(Ctx); if (!value) throw new Error("useAccessibility must be used inside AccessibilityProvider"); return value; }
