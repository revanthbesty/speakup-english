# English Mint — Mint Pets & Mintverse Master SRS / Implementation Blueprint
**Status:** Approved product direction; implementation specification. **Repository:** revanthbesty/speakup-english. **Branch:** ui/approved-design-rebuild-phase1 / draft PR #94. **Scope:** offline-first Android Jetpack Compose, no paid AI/audio services, no production merge/deploy. **Design authority:** approved English Mint UI Sprint 1–6 boards; third-party example screenshots are interaction references only, not visual assets.

## 1. Vision, users and boundaries
Mint Pets are an optional, user-selected personal learning companion integrated with the living Mintverse world. Celebrate effort, learning mastery, verified awards and returning after breaks without manipulating session length or shaming missed days. All ages may use the same functions with age-appropriate wording and presentation. No pet health, hunger, death, punishment, purchases, in-app store, loot boxes, public pet messaging, location collection, ads, or payment required. Learning content and navigation work fully without pets, sound or animation.

**Product objectives:** instant clarity during onboarding; every pet available from the start; consistent identity across tabs; truthful, non-duplicated earned rewards; delightful feedback without slowing lessons; offline deterministic state; usable in class and quiet settings. **Non-goals for MVP:** 3D/AR avatars, online multiplayer, generative pet chat, paid music, real-time emotion detection, microphone-based emotion inference.

## 2. Original pet roster (six)
| Stable pet_id | Display name | Character silhouette / signature | Personality and sample encouragement | Distinct audio motif |
|---|---|---|---|---|
| mint_fox | Mint Fox | Pointed ears, sweeping leaf-shaped tail, mint/teal | Curious explorer: “New path discovered!” | Two bright wooden-note plucks |
| buddy_panda | Buddy Panda | Round ears, soft pebble shapes, sage/cream | Calm supporter: “Nice effort. Try another way.” | Rounded marimba notes |
| spark_cat | Spark Cat | Crescent tail, expressive triangular ears, violet/coral | Playful inventor: “That idea clicked!” | Light chiming gliss |
| wise_owl | Wise Owl | Big eyes, layered feather arcs, indigo/gold | Thoughtful guide: “Let's spot the clue.” | Two soft bell notes |
| joy_rabbit | Joy Rabbit | Tall ears, spring-like hop, pink/mint | Energetic encourager: “You made progress!” | Short pizzicato pair |
| tiny_dragon | Tiny Dragon | Rounded horns, flowing winglets, turquoise/amethyst | Imaginative companion: “Your story is growing!” | Airy two-note shimmer |

**Art requirement:** create original production-ready character assets; do not reuse reference-app characters or substitute emoji as final artwork. Export neutral idle portrait, six core pose/state sprites or vector/Lottie/Rive-compatible assets, scalable silhouettes, light/dark palettes, static PNG/WebP fallback, accessibility description, and documented attribution/license. No need for external download at runtime. Product owner to visually approve all six as a set.

## 3. Onboarding journey
Existing `MintWelcomeFlow.kt` currently has four stages. Extend to **five**: (1) Welcome and motivation, (2) age/experience, (3) goal and optional name, (4) **Choose your Mint Pet**, (5) Ready and start/optional assessment, while preserving current callbacks and placement flow. On step 4, show six animated preview cards in a responsive 2-column grid (small devices: one column if text clipped) with name, temperament label, pronounceable description, sample motion/sound controls, and large selection indicator. A clear **“Not now”** option and silent preview toggle remain visible. No pet chosen defaults to none; do not silently assign or require pet consent. The learner can change their pet later from Profile > My companion. Persist only when onboarding is completed; selection should survive process recreation before completion via rememberSaveable. Back retains provisional selection; leaving onboarding does not duplicate user data. Reuse the existing age-band safety policy; no social/contact permissions requested.

### Onboarding wireflow
`Welcome → Age → Goal → Select companion [preview / mute / skip] → Ready → Home`.
Existing users see an optional, dismissible single Home prompt once, not a forced onboarding reset. Already completed onboarding and learner progress must not be overwritten. Include no-motion static card previews. A selected pet has a distinguishable outline plus a selected accessibility announcement, not color alone.

## 4. Animation vocabulary and original Mintverse mechanics
**Principle:** actions cause changes in the world, not looping trophy/confetti overlays. Distinct, short and legible. Previews may loop only when explicitly selected; normal lesson screens never loop decorative movement continuously.

| Event | Pet response | World response | Text/audio | Typical duration |
|---|---|---|---|---|
| App Home entry | calm breathing/blink once | subtle breeze on existing garden | optional muted/opt-in greeting | 250–450 ms |
| New correct answer | tilt/head perk, draw luminous word stroke | one leaf softly lights; no new progression until award confirmed | “Good thinking!” + optional tiny chime | 180–400 ms |
| Incorrect attempt | focused/questioning face, point to hint | path reveals clue, no world damage | “Try this clue…”; no sad guilt imagery | 200–350 ms |
| Retry succeeds | pet traces route back to right option | tiny leaf unfurls | “You figured it out!” | 250–450 ms |
| Verified XP award | holds a real earned token | pet plants a seed in garden | actual XP supplied by reward engine only | 350–700 ms, skippable |
| New verified badge | pet unveils an orbiting star | star permanently joins owned constellation | badge name and accessible text | 600–1000 ms, skippable |
| Lesson set mastered | pet opens a bridge between lesson islands | verified unlocked step appears | “New path opened” only if unlocked | 500–900 ms |
| Streak milestone | pet illuminates a lantern | small garden glow; never penalize absent days | truthful day count; gentle opt-in sound | 500–800 ms |
| Speaking completed | pet gestures a waveform without judging accent | bridge pulse | celebrate task completion, not invented pronunciation score | 250–500 ms |
| Returning after absence | warm wave and welcome | existing garden unchanged | “Welcome back” without shame or loss threat | 200–400 ms |
| No data/offline/error | pet provides calm wayfinding pose | no false unlocked content | plain guidance and retry where available | static |

**Companion reactions should not cover questions, answer choices, transcripts or Continue buttons.** A wrong answer never triggers a negative punishment. Randomized copy must be deterministic/safe per session, localizable, non-manipulative, and avoid “you are stronger/smarter than others” claims.

## 5. Screen integration matrix
| Screen / route | Placement | Integration / trigger | Must remain functional |
|---|---|---|---|
| First-run onboarding | Full-width preview panel; 6 options | optional choice / preview | all existing age, goal, name, placement steps |
| Home / Today | beside daily journey, integrated with MintverseGarden | stage derived from completed lessons and verified rewards | primary “Start today's adventure” remains foremost |
| Games hub | small companion invitation | contextual challenge hints | all game modes reachable |
| Grammar Arena | side mini-companion (hide on narrow screen) | check result / retry / confirmed XP | one-question Check → Continue |
| Listening & Reading | pet outside content area | end-of-task only or subtle hints | media controls, readable text, transcripts |
| Situations / Riddles / Vocabulary | corner guide | attempt vs verified correct | no double reward |
| Clips | optional small pet after playback | watch-complete only if genuinely tracked | captions and video playback |
| Speak / speech recognition | static pet during microphone active | speaking completion; suppress audio when mic active | recording / TTS / permission flow |
| Stories / co-op | story-margin idle, rare contextual cue | chapter/mastery completed | no intrusive animation over story text |
| Professional / AI Life | sophisticated compact style | meaningful task completion | never infantilize adult users |
| Rewards / Profile | companion and constellation/gallery | owned awards only; change pet | profile privacy/export/history |
| Help / Settings | compact preview and toggles | sound, music, motion preferences | offline help always accessible |
| Error / empty / offline | accessible supportive static pose | no fake success | honest error messages |

Use progressively enhanced integration behind a local feature flag until smoke tests and owner design review pass. Avoid routing changes for decorative presentation.

## 6. Proposed Kotlin / storage model
```kotlin
enum class MintPetId { MINT_FOX, BUDDY_PANDA, SPARK_CAT, WISE_OWL, JOY_RABBIT, TINY_DRAGON }
enum class MintPetReaction {
    IDLE, GREET, THINK, ENCOURAGE, CORRECT, RETRY_SUCCESS, XP_AWARDED,
    BADGE_UNLOCKED, STREAK_MILESTONE, SPEAKING_COMPLETE, OFFLINE_HELP
}
data class MintPetPreferences(
    val selectedPet: MintPetId? = null,
    val enabled: Boolean = true,
    val soundEnabled: Boolean = false,
    val musicEnabled: Boolean = false,
    val intensity: AnimationIntensity = AnimationIntensity.STANDARD,
    val companionStyle: CompanionStyle = CompanionStyle.AUTO
)
data class MintPetMoment(
    val eventId: String,
    val reaction: MintPetReaction,
    val verifiedRewardId: String? = null,
    val awardedXp: Int = 0,
    val originRoute: String,
    val happenedAtEpochMs: Long
)
```
**Persistence:** use additive keys in existing `speakup_learning` SharedPreferences for MVP: `mint_pet_id`, `mint_pet_enabled`, existing `mint_pet_sounds_enabled`, `mint_pet_music_enabled`, `mint_pet_animation_intensity`, `mint_pet_companion_style`, `mint_pet_prompt_seen`. Use stable lowercase IDs, versioned normalization, and safe unknown-value fallback to no pet. Never change package ID, legacy XP keys, mastery IDs, onboarding finish semantics or sync paths. Existing `MintverseGrowth.stage(completed)` continues mapping 0/1/3/7/14 verified completed lessons; stages are visual only.

**Event pipeline:** real lesson/reward engine produces a verified once-only event → `MintPetEventMapper` chooses a reaction → `MintPetMomentCoordinator` deduplicates by real award/event key → bounded animation queue (at most one moment visible) → UI render plus audio only after separate preference/context gates. Never award XP inside animation, never infer badges from visual stage. Consume events, not every recomposition. On rotation, never replay high-value reward reveals already acknowledged; app restart should restore the resulting garden state from persisted learning data. For MVP avoid persistent history of incidental idle interactions.

**Recommended files:** `MintPetCatalog.kt`, `MintPetPreferences.kt`, `MintPetPickerScreen.kt`, `MintPetCompanion.kt`, `MintPetAnimation.kt`, `MintPetEventMapper.kt`, `MintPetMomentCoordinator.kt`, `MintPetAudioController.kt`, `MintPetSettings.kt`, `MintverseGarden.kt`, `MintPet*Test.kt`. Respect established MainActivity and Compose conventions and move code from monolith safely.

## 7. Sound and music system
A unique original motif per pet, small variation per emotional reaction, and calm unlock cue only for **verified** achievements. Sound effects and background music use independent persistent toggles, **both off by default**. Audio never autoplays during onboarding; explicit preview is allowed. Play max one short cue per event (avoid sound spam), cap volume and overlapping voices, honor silent/vibrate, DND and muted media stream. Speaking, recording, listening, video, TTS, accessibility announcements and other apps' audio focus take precedence. Background music requires Android audio-focus lifecycle handling, pause/resume on app foreground/background, duck or pause for lessons and focus loss, and an independent volume setting. Use owned/licensed bundled loops, no expensive streaming services. No audio ad or subscription feature. Headphones and phone speakers should both work.

**Existing code:** `MintPetSound.kt` currently offers locally synthesized short demo tones and Profile preview; it is not yet a robust focus-aware production audio controller. This SRS requires implementation and device validation before broad auto-triggering.

## 8. UX, accessibility, safety and performance rules
- Animations short and opt-out, meaningful UI never blocked. Default motion: micro 150–300ms, world changes 250–450ms; rare milestone scene max ~1000ms with Skip. No seizure-risk flashing, repetitive bobbing or high contrast strobe.
- Honor existing `mint_reduce_motion`, Android animator duration scale = 0, battery saver, low-performance mode and lifecycle pauses. Provide equally understandable static state and visual text feedback without audio.
- Screen reader semantics announce selected pet, reaction text and earned awards once; decorative Canvas art excluded from redundant announcements. Focus remains on current learning task. Meet minimum 48dp targets, adaptable font scaling to 200%, small devices and dark mode contrast.
- Child-friendly defaults and privacy: no microphone/audio capture for pets, no contact/social sharing, no external trackers, no mood inference, no body/health judgments. Store only preferences and existing verified learning achievements locally.
- Adult/professional style preference may reduce companion size and exuberance; fully hideable. Do not create fake pet suffering, hunger or timed obligations.
- Performance budgets (targets to verify on API 29 and 35): keep Compose frame work under normal device frame deadlines; no blocking draw operations or main-thread audio generation; cap concurrent effects; resource-safe loading, static fallback and test memory use in long sessions. Monitor debug build size after adding six bundled art/audio packs.

## 9. Acceptance and analytics
Functional acceptance: onboarding choose/skip/change persists across process death; six available original pets render correctly; settings persist; one correct/incorrect/retry cycle triggers suitable distinct reaction; verified XP/badges trigger once, no duplicates after rotation; saved progression unchanged; offline all works; no interruptions during Speak, TTS and Clips; every main tab/back remains correct. Accessibility matrix: 320dp, 360dp, 412dp + tablet, dark/light/system/night, 100/130/200% font, TalkBack, reduced-motion, mute/vibrate/DND, low battery, resume/background, captions/recording overlap. Visual acceptance: screenshots for all six pets in onboarding/Home/Profile, animation storyboards vs approved English Mint boards, owner review. Tests include catalog ID stability, preference migration, event mapping, queue deduplication, actual Compose state, screenshot/golden smoke, Android API 29 and 35 CI debug APK. No unsupported “all tests passed” claims.

Privacy analytics optional local aggregate: companion selected, preference enabled, verified scene displayed and skipped, UI frame/render errors. Do not transmit pet interactions or child profiles off device without separate consent, data review and release approvals.

## 10. Phased implementation plan with explicit exit gates
| Phase | Work | Dependencies / exit gate |
|---|---|---|
| P0 — Spec and assets | finalize six pet silhouettes, moodboards, poses, motifs, onboarding boards and localization strings | owner approves original designs and licenses |
| P1 — Sprint 1 foundation | add typed catalog, preference migration/normalization, animation/audio policies, motion fallback, tests | CI/API29+35 passes; existing Sprint 1 nav/themes unchanged |
| P2 — Onboarding and Settings | integrate optional pet selection into real four-step `MintWelcomeFlow` without breaking placement flow; Profile change/hide, sound/music controls | fresh and upgrading user smoke, preference persistence |
| P3 — Home Mintverse | selected pet rendering, garden interactions and honest milestone scene using `MintverseGarden` | snapshot comparisons, no invented XP, 60fps target measured |
| P4 — Learning reactions | connect Grammar/Games/Reading/Listening/Situations and Speak to real checked/completion events, no overlap with media | dedupe tests, feedback flow, microphone coexistence |
| P5 — Audio identity | six original motifs, owned ambient loops, focus-safe controller, independent volume/preferences | quiet mode and audio-interruption device QA |
| P6 — Rewards and profile | constellation, badge reveal, accessory visuals based on owned awards, adult style | events verified and no reward duplication |
| P7 — Application-wide QA | audit all routes from Sprint1–66 roadmap, screenshots, accessibility, battery/performance, localization and regression | signed-off matrix + owner-approved visual parity; still no merge without explicit instruction |

**Definition of done for the feature:** all six selectable pet visuals accepted; authentic milestone events; correct sound/motion/privacy options; zero regressions; passing CI/API builds/device review; PR release notes and owner sign-off. Until then mark individual phases as coded / verified / blocked, not “Mint Pets complete.”

## 11. Actual status as of 19 October 2026 (not future work)
- Implemented: `MintverseGarden.kt` display-only stage-driven garden integrated in Today/Home; `MintAchievementCelebration` reusable reduced-motion UI; `MintPetSound.kt` locally generated preview cue; `mint_pet_sounds_enabled` toggle and preview on Profile.
- **Not implemented:** full roster assets, selectable onboarded pet, pet companion rendering, event reactions tied to lessons, original pet motifs, production music player/audio focus, customization, constellation and cross-app integration.
- Draft PR #94; do not merge or deploy without explicit instruction. Exact newest commit CI and APK must be checked separately after edits.

## 12. Incremental implementation — offline companion learning (19 October 2026)
- Added `MintPetLearningEngine.kt` with five fixed offline adventure blueprints and a deterministic mood/skill suggestion mapper: vocabulary garden, grammar bridge, reading lantern, listening echo cave and speaking gateway.
- Wired a **Mintverse mini-adventure invitation** into existing Home / Today, using the already stored completed lesson count. The action currently opens **Games**, not a special pet story mission. Keep that text honest.
- Added `MintPetLearningEngineTest.kt` unit coverage for catalog uniqueness, welcome/encouragement, selected skill and garden progression.
- **Not yet wired:** real per-skill mistakes/correct answer counters, a selectable pet avatar, curated quest question banks, pet teaching dialogs, mission deep links, rewards integration or personalized adaptive lessons. Home passes zero for recent attempts until genuine source data is mapped. Avoid claiming intelligent adaptation is live.
- Verify CI and Android APK at the exact PR head before sign-off. Continue Sprint 1 visual/device acceptance and leave the PR draft.

## 13. Selected pet on actual learning-completion panels (19 October 2026)
- `MintActivityCompletion.kt` now reads the saved `mint_pet_id` in the same legacy preferences file used by onboarding/Profile and displays that companion's existing portrait in the verified activity completion panel.
- The message differentiates **new earned XP** from **review of previously completed content**; no XP or badge side effects were added. Without a selected pet, the previous completion flow renders unchanged.
- Five existing `MintActivityCompletion` integrations in MainActivity benefit from this shared change. This is a static contextual companion reaction, not yet expressive pose animation or automatic sound. Reduced-motion input is passed to the portrait.
- Remaining QA: test fresh award vs review, pet selection changes, no-pet display, app restart, audio coexistence, large font, and CI on exact commit.

## 14. Distinct animated pet achievement reaction
- Added a bounded, one-shot 520ms achievement pose to the shared `MintPetPortrait`, keyed by pet ID with varying rotation and gentle vertical movement. Each of six pets has its own motion angle; this is an **interim original geometric illustration**, not final approved character art.
- `MintActivityCompletion` requests animation only when the existing verified-reward predicate `fresh` is true. Reviews remain static and receive supportive text, without re-awarding XP.
- Existing `reducedMotion` flag bypasses animation and renders the legible pose statically. No continuous idle animation or sound autoplay has been added.
- Remaining: design-accurate pet assets, correct/wrong answer contextual poses, event-driven sound integration, actual screenshot/device animation QA and full pet art signoff.

## 14. Premium animation/sound sprint progress — 19 October 2026
- Implemented this pass: six distinct **locally synthesized** pet pitch signatures for Greeting, Encouragement and Achievement in `MintPetSound.kt`. Existing Profile pet-sound opt-in now includes three manual previews for the saved pet. All tones remain disabled when the existing pet-sound toggle is off; existing silent mode and muted media checks are preserved. Added safer AudioTrack cleanup.
- Preserved: existing six geometric Compose portraits, short checked-answer animations across Situations, Grammar, Vocabulary, Riddles, Reading and Listening, once-only XP logic, navigation and stored learner progress.
- Not implemented: production-quality character art or separate sprite sets, audio-focus ownership and automatic sound synchronized to learning events, licensed ambient background music/player and separate music toggle, full all-screen pet integration, API 29/35 instrumentation/device video QA, physical screenshots versus approved boards. Those must not be represented as finished.
- Verification requirement: use CI and APK from the **exact latest code HEAD**, plus manual Profile preview checks with each pet and silent/vibrate/volume-zero modes. No merge or deployment approved.
