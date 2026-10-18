# English Mint — Sprint 8 acceptance and release-hardening report

Base: verified Sprint 7 branch `feature/english-mint-sprint-7`.
Branch: `feature/english-mint-sprint-8`.

## Implemented and traceable
- **72 unique authored playable Mint Quests** in three content modules: `MintGameCatalog.definitions` (24 retained Sprint 3 quests), `MintExpansionPack` (24 new listening/story-decision quests) and `MintVisualVocabularyPack` (24 new word-connection/picture-meaning quests).
- **Eight new reusable content-driven mechanics:** Choose Reply, Missing Word, Word Scramble, Sentence Builder, Listen and Choose, Story Decision, Word Connect and Picture Match. Each mechanic offers age-filtered Easy/Medium/Hard options. The prior eight game experiences are retained (they are not counted as part of the 72 authored quest items).
- All newly authored items have explicit valid answers, prompts, choices/tokens, explanations, hints, speaking follow-up and age/difficulty/skill metadata.
- The listener can play original text via existing Android TTS and reveal the complete transcript for accessibility, with a non-audio alternative.
- The picture game includes large illustrative emoji plus a plain-language question, rather than relying on color or image recognition alone.
- The pure `MintDailyQuest` utility selects a day-stable age-appropriate item and prefers unmastered quests. It does not demand consecutive-day use, reset XP or manipulate streaks.
- Catalog `byId` and difficulty lookups use once-built indexes; quest browse selection is memoized across Compose recompositions.
- The original XP key `total_xp`, `mint_game_awards` versioned reward IDs, other games, learning pathways, progress and badges are unchanged. Prior earned quests remain earned. APK identity and local storage namespace unchanged.
- Some professional content requires age 18+, in addition to existing base age gates.

## Automated acceptance
- `MintExpandedCatalogTest`: exactly 72 unique item IDs, all content valid with solvable correct answers, eight mechanics, all three difficulties, author-written new packs, baseline reward ID stability and age filters.
- `MintDailyQuestTest`: deterministic day-level selection, unmastered preference and safe no-content behavior.
- `MintGameEngineTest`: existing answer evaluation, anti-duplicate first-mastery XP, and age regression tests updated for catalog expansion.
- Run Android unit tests, lint and assemble APK via `.github/workflows/android-debug-apk.yml`, monorepo CI and release gates on the PR head.
- Device-level font scaling, TalkBack, keyboard/switch navigation, picture semantics, screen recording, runtime performance, low-memory launch, offline activity and upgrade over the original installed APK remain for the user's post-Sprint-10 hands-on review.

## Honest product gaps / non-deliverables
- **72 / 500** uniquely authored Mint Quest variants; the SRS 500+ curated-variant ambition is NOT achieved. More educationally reviewed content, mechanics and content packs are required.
- Eight new reusable mechanics are not fifty. Previous legacy game experiences are retained, not renamed as fifty separate engines.
- Pack authoring is Kotlin-local, not an approved CMS workflow or downloaded versioned JSON database. Future offline pack migration and content rollback require implementation.
- No claim of external educator/content licensing review, professional language examination assessment, real AI pronunciation score, real community moderation or enabled payments.
- No analytics-proven performance p95, accessibility certification, Play Store security review or production rollout is claimed. Automated lint/build success is not a replacement for these release gates.

## Safe integration and release boundaries
No production branch merges, release signing keys, third-party cloud costs or production deployments. Existing `com.speakup.app` ID and `speakup_learning` preference file remain unchanged. Stacked PR reviewed against Sprint 7. User will install/review app after Sprint 10 as agreed.
