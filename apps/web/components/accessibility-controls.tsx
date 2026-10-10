"use client";
import { useAccessibility } from "./accessibility-provider";
export function AccessibilityControls() {
  const a = useAccessibility();
  return <details className="a11y-menu"><summary>{a.t("accessibility")}</summary><div className="a11y-panel"><label>Text size<select value={a.textScale} onChange={e=>a.setPreference("textScale", e.target.value as typeof a.textScale)}><option value="normal">Normal</option><option value="large">Large</option><option value="xlarge">Extra large</option></select></label><label><input type="checkbox" checked={a.reducedMotion} onChange={e=>a.setPreference("reducedMotion", e.target.checked)}/> Reduce motion</label><label><input type="checkbox" checked={a.highContrast} onChange={e=>a.setPreference("highContrast", e.target.checked)}/> High contrast</label></div></details>;
}
