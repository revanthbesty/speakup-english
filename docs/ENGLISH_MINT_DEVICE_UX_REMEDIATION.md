# English Mint — Installed-device UX rescue (feedback after Sprint 10)

## Device findings from user screenshots
The first real-device review exposed multiple high-severity problems despite green Android unit tests/lint:
- Default dark theme renders unreadable black text on nearly-black screens in Feed, Speaking and listening prompts.
- Status bar content conflicts with the app header at the top of screens.
- Home has repeated primary actions, verbose duplicated sections and no clear learning route.
- Games displays multiple rows of chips, game worlds, mechanics, a daily suggestion and large instruction paragraphs simultaneously.
- Speaking shows the full level selector, topic selector, multi-step dialogue, lengthy disclaimers and recognizer text entry together.
- Profile shows membership, appearance, privacy and all preference chips in one unstructured scroll.

## Remediation in this PR
1. Complete Android Material 3 color role values for both themes, including `onBackground`, `onSurfaceVariant`, container variants and outline roles. Generic Material Text now inherits readable foreground in both modes.
2. Add status-bar safe-area padding to the app root.
3. Home now starts with one primary CTA **Start my first conversation**, an explicit three-step learning map, and a direct **Explore games** alternative. The previous analytics and detailed academy cards are preserved behind **See progress and more options**.
4. Games opens as one playable quest. Game modes, difficulty and mechanic selector are hidden until the learner selects **Change game or category**.
5. Guided speaking starts on a single conversation with a short purpose statement; topic and level controls are disclosed by **Change topic or level**.
6. Profile opens with the user's learning summary, progress button, expandable **Edit learning preferences** and **Appearance, privacy & membership**. Older settings remain accessible and persist as before. Profile save also persists `mint_age_band`.

## Product rule adopted
- One main action per screen. Details and advanced settings are opt-in.
- Clear, short contextual directions. Don't front-load paragraphs of legal/technical guidance; retain required privacy consent at the moment it matters (microphone and export).
- Avoid stacked redundant CTAs, especially on Home.
- Keep all original learning data, navigation, games, preferences, privacy, billing safeguards and social restrictions.

## Verification
Run Android unit tests, lint and debug APK, repository CI and release gates on the PR head. Green CI proves compilation/regression checks, **not** sufficient visual accessibility proof.

## User device acceptance required
- Check Feed, Speak, listening playback and every modal in Dark, Light, System and Night mode for readable text and controls.
- Confirm top header does not overlap status bar.
- Home: identify next action within three seconds; tap speaking and games; toggle dashboard details.
- Games: play default question, check answer and hints, expand filters, change level and mechanic.
- Speak: follow three dialogue turns; change topic/level on demand; verify mic/privacy dialog and typed fallback.
- Profile: verify settings and learning preferences toggles, change age band, save, reopen, and confirm changes.
- Repeat with 200% display/font, TalkBack, narrow-screen devices and reduced-motion.
- Never uninstall the existing app to force incompatible debug signing: protect learner progress.

Do not merge/publish without owner review and device QA. This is UX remediation, not a claim of complete fulfillment of all originally outstanding Master SRS features.
