import Link from "next/link";
import { AccessibilityControls } from "./accessibility-controls";
import { LocaleSelector } from "./locale-selector";
import { NavLabels } from "./nav-labels";
export function Nav() { return <header className="topbar"><Link className="brand" href="/home">SpeakUp</Link><nav aria-label="Primary"><NavLabels /></nav><LocaleSelector /><AccessibilityControls /><Link className="profile" href="/progress" aria-label="Open profile"><NavLabels meOnly /></Link></header>; }
