export type AccessibilityPreferences = {
  textScale: "normal" | "large" | "xlarge";
  reducedMotion: boolean;
  highContrast: boolean;
  focusVisible: boolean;
};
export const DEFAULT_ACCESSIBILITY: AccessibilityPreferences = { textScale: "normal", reducedMotion: false, highContrast: false, focusVisible: true };
