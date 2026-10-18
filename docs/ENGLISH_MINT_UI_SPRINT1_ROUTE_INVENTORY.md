# English Mint — UI-only Sprint 1 route and acceptance inventory

Design source: `English_Mint_UI_Sprint1_Codex_Prompt(1).txt` and visual board provided by the product owner. Design is a concept, not an approval of device screenshots.

Baseline: `fix/english-mint-ux-rescue-consolidated` (PR #16), on top of Sprint 10. Branch intentionally stacks on the unmerged rescue; do not merge into main or independently merge overlapping PRs #14/#15.

## Main navigation

| User label | Internal route | Entry | Notes |
| --- | --- | --- | --- |
| Home | Today | bottom tab | daily adventure, Learn/Review shortcuts |
| Games | Games | bottom tab | retained Mint Quests and legacy game modes |
| Clips | Feed | bottom tab | underlying feed route deliberately unchanged |
| Speak | Speak | bottom tab | guided conversation and legacy lessons |
| Profile | Profile | bottom tab | settings and profile disclosure |

## Secondary routes / back behavior

| Route | Parent / entry | Back action |
| --- | --- | --- |
| Learn | Home (Learn a phrase) | Android Back goes Home |
| Review | Home (View progress) | Android Back goes Home |
| Privacy | Profile > Edit profile and settings > Privacy | Android Back goes Home (nested stack improvement pending) |
| Membership | Profile > Edit profile and settings > Compare | Android Back goes Home (nested stack improvement pending) |
| Speaking Lessons | Speak > Speaking lessons | Switch back to Conversations |
| Game modes | Games > Change game or difficulty | Select other mode or return to Games tab |

Selected main tab persists to existing `speakup_learning` preferences under the new additive `mint_selected_tab` key. Invalid saved route falls back to Home. Internal route IDs, established XP/reward IDs, legacy lesson content and user records remain unchanged.

## UI changes
- Brand palette aligned with conceptual UI Sprint 1 board: off-white `#F8FAFC`, deep night `#0F172A`, night card `#1E293B` and accessible mint variants.
- Bottom labels and route ordering updated to Home, Games, Clips, Speak, Profile.
- Main-tab active indicator recognizes Learn and Review as Home, Privacy and Membership as Profile.
- Hardware Back from non-Home destinations returns Home. Further nested stack preservation is a known improvement.
- Restores selected main tab across app recreation.
- Existing prior UX fixes remain; no new LLM, social, billing or backends.

## QA gates (not equivalent to physical acceptance)
- [ ] Android lint, unit tests, debug APK build on exact branch HEAD
- [ ] Monorepo lint, typecheck, tests and build
- [ ] On-device 320dp/360dp/412dp, gesture/3-button navigation and status bar
- [ ] Font scale 1x/1.3x/2x; TalkBack order, contrast, touch targets and landscape
- [ ] Device screenshot review of Home/Games/Clips/Speak/Profile plus onboarding
- [ ] Verify old stored progress, XP, profile, mastered keys and local reports unchanged after a matching-signed update
- [ ] Confirm the new navigator does not introduce duplicate destinations or inaccessible screens
- [ ] Obtain product-owner approval before merge

## Limitations
This sprint applies foundation/navigation changes; it does not implement the full pixel-perfect board, new artwork/assets, full reusable design primitives, complete nested navigation stacks or instrumentation screenshots. Those remain open acceptance gaps. No production merge or payment activation is authorized.
