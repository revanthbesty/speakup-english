# English Mint — Approved UI design remediation, Phase 1

## Source of truth and rejection evidence
Product owner supplied 14 device screenshots from the latest English Mint Android build showing text-heavy onboarding, crowded Home/Games and Grammar Arena, repeated progress/release notices, minimalist text-only navigation and profile/settings overload. Owner explicitly REJECTED the shipped UI appearance and requested the earlier approved visual design.

Approved design references in Library: `EnglishMint UI Sprint 1 Design System Poster.png`, `EnglishMint UI_UX Modernisation Sprint Plan.png`, `EnglishMint UI Sprint 6 Design Board.png`, `English_Mint_Master_SRS_with_UIUX_Designs.docx`, and `English_Mint_UI_Sprint6_Design_and_Prompt.docx`.

Key intended look: playful mint/teal and purple, pastel illustration panels, rounded selection cards, obvious single CTA, mascot/illustrative appeal, exactly five persistent nav tabs, responsive dark/light and accessibility.

## This reviewable first increment
- Completely replaced the dense original `MintWelcomeFlow` layout with colorful pastel onboarding art panels and premium selectable profile/goal cards, keeping all age bands, original goals, four steps, optional placement and inherited persistence callbacks intact.
- Changed the Home reading order: user greeting, one prominent 5-minute adventure CTA, then Continue Learning and secondary discovery.
- Moved detailed Garden collectibles, Coach opt-in and experimental routes to an explicit **Explore more activities and rewards** disclosure rather than crowding the first viewport. All existing destinations and behavior are still reachable.
- Updated bottom navigation symbols to recognizable five-tab Home/Games/Clips/Speak/Profile labels; internal destination IDs remain unchanged.
- No progress, reward calculation, grammar answers, business logic, paid inference, storage or packages changed.

## Remaining mandatory UI rebuild
The approved design is **not yet comprehensively implemented**. This first PR does not replace all Games, Grammar, Stories, Clips, Speaking, Profile, Progress, Membership, Settings or Professional surfaces. Later UI-only increments must carry over the exact design tokens/assets, structured app bars, larger touch targets, illustrations, concise help, bottom-nav indicator and full accessibility/contrast proof. Avoid fake availability and unearned user accomplishments.

## Acceptance
- Onboarding 1–4: profile age and goal choices, skip, back, optional placement and local SavedPreferences retention.
- Home: unique primary Start adventure button visible before Garden; Explore more opens every existing advanced pilot; collapsed view minimizes clutter.
- Navigation: five original routes preserved; TalkBack labels and focus order; status/navigation bar insets; Android API 29/35 tests; large fonts and scrollability; system dark/light/night.
- Existing Grammar correct-answer grading, bookmarks, XP/Bloom anti-duplication, Speak opt-in, Clips and Profile routes unchanged.
- Preview new screenshots on target phone and request explicit visual sign-off.
- Do not merge into main or deploy.
