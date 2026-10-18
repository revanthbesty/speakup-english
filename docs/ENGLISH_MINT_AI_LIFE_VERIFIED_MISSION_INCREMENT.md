# English Mint — verified AI Life daily mission integration

## Implemented against integrated Sprint 66 draft
- A new privacy-minimal local ledger stores date + stable mission ID only in the existing `speakup_learning` SharedPreferences, in a separate key: `mint_life_daily_completion_v1`.
- Current supported mission is the original, fully authored offline café conversation `life-cafe-conversation-v1` (three dialogue turns). A new completion is recorded **only** when the final turn is answered correctly after checking the response.
- Completion is once per mission per local calendar day. Replaying does not duplicate the same-day evidence or mint rewards.
- AI Life Home shows only **1 available authored verified mission**, a real today completion count and recorded practice days over the last seven local dates; no invented goals, streaks, speaking scores, grammar mastery or proficiency ratings.
- A confirm-to-delete control removes only this AI Life daily mission ledger from on-device preferences; it leaves existing lessons, reward collections, account preferences and saved activity records unchanged.
- Pure unit tests cover unverified rejection, unknown/invalid IDs, invalid dates, once-only deduplication, future/expired dates in the seven-day count, and cleanup of malformed entries.
- No paid AI API calls, no model dependency, transcript storage, network permission, microphone access, migration or publishing.

## Status / quality gates
This is a focused repair to the broader Sprint 57–66 pilot, **not** full Sprint 58 daily planning, voice practice, 57 evidence-to-profile integration, 60 analytics, real camera OCR or 64 authenticated family/co-op. For real device acceptance, confirm the Compose screen and SharedPreferences data using API29 and API35 upgrade installs. Test airplane mode, daylight changes and reboot, app restart, once-per-day completion, replay, deletion, restored legacy `speakup_learning` data, dark/light/night, TalkBack and large fonts.

The existing GitHub workflow builds APK and runs unit/lint checks, but does not demonstrate API29/35 emulator instrumentation or physical device acceptance. Manual approval is needed before calling the app release-ready.

Branch stacked on draft integration PR #88. Do not merge, deploy or activate paid AI.
