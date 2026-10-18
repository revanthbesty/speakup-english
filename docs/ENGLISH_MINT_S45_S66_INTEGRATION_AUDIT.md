# English Mint — Sprints 45–66 integration preview and acceptance tracker

## What this branch does
Based on AI Life Sprint 66 draft head `sprint66-life-release-readiness`, this branch **copies** the Professional English Sprints 46–48 policy sources/tests and documentation that were on a separate draft stack, and adds an in-app Mint Buddy Lite practice page in AI Life.

The Professional 46–48 PRs (#85–87) remain separate draft review records; there is no merge to protected `main`. Do not merge parallel branches blindly: these copies must be reconciled during full integration.

## Sprint coverage

| Sprint | Development evidence | Blocked completion criteria |
|---|---|---|
| 45 | deterministic recommendation and local ID model, PR66 | persistence, valid mastery and rewards |
| 46 | editorial approval gate plus tests, PR85 | authenticated CMS, reviewer history, secure publishing |
| 47 | offline/free learning policy plus tests, PR86 | device TalkBack, download safety, media caching, billing audit |
| 48 | release evidence gate plus tests, PR87 | exact-head device and migration QA, rollback, human signoff |
| 49 | manifest capability audit, PR67 | device license/AI baseline benchmark and full dependency review |
| 50 | fail-closed local AI router, PR68 | application-wide routing and actual network egress measurements |
| 51 | offline speech consent policy, PR69 | verified offline STT/TTS implementation, permissions and accuracy tests |
| 52 | model checksum/license gate, PR70 | approved model, runtime, downloads and benchmarks |
| 53 | scripted Mint Buddy model, PR71 | this integration branch adds **text-only UI**; safe real local LLM unimplemented |
| 54 | answer-preserving offline adapters, PR72 | connect into all features with editorial quality checks |
| 55 | device Lite eligibility policy, PR73 | instrumented RAM, thermal, TalkBack and UX evaluation |
| 56 | zero-AI release-evidence gate, PR74 | true zero-egress/offline audit, device regression, owner approval |
| 57 | versioned learner evidence model, PR75 | authenticated migration, real achievement source integration |
| 58 | daily mission policy, PR76 | real eligible published mission catalog, completion ledger |
| 59 | voice passport consent rules, PR77 | voice recording/playback/export/deletion with device privacy QA |
| 60 | weekly summary policy, PR78 | real, opt-in observation records and accurate UI |
| 61 | 3-turn Virtual City cafe pilot, PR79 | further reviewed venues/dialogues/voice access |
| 62 | branching story graph pilot, PR80 | expanded reviewed narrative content, persistence |
| 63 | manual camera vocabulary fallback, PR81 | opted-in camera pipeline, recognized object QA and deletion |
| 64 | guardian permission/expiry policy, PR82 | authenticated, moderated and child-safe group experience |
| 65 | resilience checks, PR83 | low-end physical-device and full accessibility acceptance |
| 66 | AI Life no-go release gates and three scripted scenes, PR84 | all prior missing functionality and signed-off release evidence |

## Rules
- ₹0 paid AI APIs and tokens, no paid fallback.
- Draft branches only, no deployment, publication or merge to main.
- Do not claim that the model-only guards are feature-complete.
- No evidence of full QA for 45–66 until exact-head CI, Android APK/artifact, API29/35 instrumentation, TalkBack, offline device tests and SRS sign-off are recorded.
- Review the old reward ledger and legacy saved progress before schema or content ID changes.

## Manual smoke path
Home -> AI Life -> Mint Buddy -> enter typed sentence -> switch level -> clear. Confirm no transcript persists after leaving/reopening.
Home -> AI Life -> cafe -> three turns; branching story -> different choice; manual Words no camera request. Home -> Professional -> original lessons and all practice modules.
Check offline/no network, themes, TalkBack, larger fonts, device rotation and any old progress/rewards.
