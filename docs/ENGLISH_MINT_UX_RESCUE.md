# English Mint — usability rescue after installed-device review

## Screenshot-based findings
1. Feed, guided conversation and listening labels rendered dark foregrounds against a dark background, severely limiting readability.
2. Home offered many competing actions and four statistics before explaining where beginners should start.
3. Games displayed category and difficulty selectors as multiple long rows before gameplay.
4. Speaking displayed both difficulty and scenario selectors before the dialogue.
5. Profile exposed privacy, plans, themes, child/social disclosures, age, goal, pathway and daily goal together in an exceptionally long first view.
6. User had no prominent "start here" action or clear next step.

## Implemented in feature/english-mint-ux-rescue
- Set explicit dark palette `onBackground`, `onSurfaceVariant`, container foreground/background and `onPrimary` colors.
- Wrap Feed and guided Speaking Studio in Material theme-aware `Surface` containers to propagate readable `LocalContentColor`.
- Replace overloaded Home with a primary 3-turn speaking CTA, three optional Explore links and a compact progress summary; existing feature routes and saved data remain unchanged.
- On guided conversation, show scenario/level choices only when "Change level or topic" is selected; simpler titles and instructions.
- On Games, hide category/difficulty selectors by default under "Change game or difficulty"; retain all eight mechanics and existing game categories.
- On Profile, present learner summary and single "Edit profile and settings" action, keeping privacy, memberships, appearance and full preferences available on demand.
- No storage schema or billing changes, no removed game content, no altered earned XP.

## Verification and acceptance
- Run Android JUnit, lint and debug APK assembly, monorepo CI and release gates on latest PR head.
- Install on safe test device with compatible signature, compare supplied screenshots: all foregrounds legible, no blank or clipped modal, touch targets 48dp, long labels support large fonts.
- TalkBack and accessibility contrast checks, font scale >=200%, full-system dark/light, narrow screen rotation and speech recognition failure states still require on-device verification.
- No claim that a compiled APK alone verifies user comprehension. Product owner decides if navigation is intuitive and appearance premium.
- Future dedicated product design pass should unify icons and graphics, introduce an illustrated mascot, redesign assessments and content typography, and consolidate the giant MainActivity into modular navigation/UI components.
