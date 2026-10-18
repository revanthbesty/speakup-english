# English Mint — Sprint 3 Games Hub delivery and acceptance

## Baseline and safety
Branch: `feature/english-mint-sprint-3`, based on verified Sprint 2. This sprint extends (does not replace) the current Compose Games screen. It preserves the original `com.speakup.app` application identity, original `speakup_learning` preferences, existing game modes, lessons, XP, badges, speaking attempts and earned puzzle rewards.

## New production code in this PR
- `MintGameEngine.kt`: reusable typed, version-aware definition schema, answer evaluator, first-time reward result with stable `id + version` keys, difficulty/skill/mechanic types. No AI or network dependency.
- `MintGameCatalog.kt`: 24 manually authored initial content items (4 actual mechanics × 3 difficulty bands × 2 curated activities). The catalog is separate from the engine; future content packs can be written without editing engine code.
- `MainActivity.kt`: Mint Quest Arcade integrated as default Games mode. Existing Word Builder, Riddles, Vocabulary, Grammar, Situations, Listening, Reading and Dialogues are retained. Includes difficulty/mechanic browsing, progress, answer selection or token ordering, hints, undo/reset, checking, explanation, next activity, XP and speaking follow-up.
- Games use age-suitability tags: Easy 4+, Medium 8+, Hard 13+. If a combination has no eligible content, show an honest empty state instead of exposing an older-audience pack.
- Speaking follow-up carries the authored game speaking prompt into the existing private Speaking Studio. A game speaking prompt is **not** given an invented pronunciation score; speech-to-text uncertainty is communicated.
- First correct solve yields 15 XP; further correct replays award 0. Rewards are persisted by versioned event key to `mint_game_awards` within the original preference file; no legacy keys are removed.

## Tests
- `MintGameEngineTest`: distinct IDs, definition validation, all 24 playable items, 4 mechanics × 3 difficulties, normalized exact answers, wrong answers, idempotent XP, speaking prompt/hint quality, age restrictions.
- Existing tests (placement, experience policy, identity, listening regressions) run unchanged.
- Workflow: `testDebugUnitTest`, `lintDebug`, `assembleDebug` and APK/report artifacts; monorepo CI and release gates.

## Honest coverage
- 4 genuinely implemented new mechanics and 24 authored examples, **not** 500 games and **not** 50 mechanics.
- Separate content pack definitions enable scaling, but external content-pack download, JSON schema/CMS, localization, educator approval and safe sync are later work.
- Questions use static, author-written answers and supportive feedback; no claim of AI evaluation or authentic phonetic scoring.
- Current game ages are content eligibility defaults, not a substitute for verified child consent and guardian controls. Public social features remain disabled.
- Device accessibility audits, performance and compatibility acceptance remain deferred until the user reviews the installed app after Sprint 10, not misrepresented as performed.

## Exit gate
CI must pass with Android unit tests/lint/debug APK and monorepo checks on final PR head. Do not merge, deploy, publish, enable payments, or request device installation. Keep this as a reviewable stacked pull request.
