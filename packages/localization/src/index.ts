export type SupportedLocale = "en" | "hi" | "ta" | "ar";
export const SUPPORTED_LOCALES: SupportedLocale[] = ["en", "hi", "ta", "ar"];
export const RTL_LOCALES = new Set<SupportedLocale>(["ar"]);

const messages = {
  en: { home: "Home", journey: "Journey", progress: "Progress", me: "Me", language: "Language", accessibility: "Accessibility", save: "Save", close: "Close" },
  hi: { home: "होम", journey: "यात्रा", progress: "प्रगति", me: "मैं", language: "भाषा", accessibility: "सुलभता", save: "सहेजें", close: "बंद करें" },
  ta: { home: "முகப்பு", journey: "பயணம்", progress: "முன்னேற்றம்", me: "நான்", language: "மொழி", accessibility: "அணுகல்தன்மை", save: "சேமி", close: "மூடு" },
  ar: { home: "الرئيسية", journey: "المسار", progress: "التقدم", me: "حسابي", language: "اللغة", accessibility: "إمكانية الوصول", save: "حفظ", close: "إغلاق" }
} as const;

export type MessageKey = keyof typeof messages.en;
export function translate(locale: SupportedLocale, key: MessageKey): string {
  return messages[locale][key] ?? messages.en[key];
}
export function isRtl(locale: SupportedLocale): boolean { return RTL_LOCALES.has(locale); }
export function formatNumber(value: number, locale: SupportedLocale): string { return new Intl.NumberFormat(locale).format(value); }
export function formatDate(value: string | number | Date, locale: SupportedLocale): string { return new Intl.DateTimeFormat(locale, { dateStyle: "medium" }).format(new Date(value)); }
