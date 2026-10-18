# English Mint — UI Sprint 5: professional learning audit

## Integration
Draft branch `feature/english-mint-ui-sprint-5-professional` stacks on unmerged Sprint 4 PR #20, Sprint 3 PR #19, Sprint 2 PR #18, Sprint 1 PR #17, UX rescue PR #16. No production merge.

## Source-supported capabilities
- The existing Android application provides four general lessons, skill practice, guided conversations, local learner goals and XP/review data.
- No verified published catalog of professional courses, workplace video content, employment certificates or live AI Life inference service was identified in the Android UI audit.
- This sprint does not create any of these absent capabilities; the concept board's figures, artwork and certificates are not actual source data.

## Route matrix
| Entry | Target | Supported | Back / fallback |
| --- | --- | --- | --- |
| Home > Explore Professional English | Professional Hub (new nested page) | Yes | Back goes Home |
| Hub > Continue learning | Existing Learn route | Yes | Android Back Home |
| Hub > Career topics | Professional > industries list | UI catalog only | Button back to Professional Hub |
| Industry > Existing lessons | Existing Learn route | Yes | Android Back Home |
| Industry > Practise speaking | Existing Speak > Conversations | Yes | Android Back Home |
| Hub > Everyday real-life scenarios | AI Life informational local fallback | Live AI unavailable | Existing guided conversations or lessons |
| Hub > Progress | Existing Review route | Yes | Android Back Home |

The industry list is intentionally an availability/discovery list, not a claim of published courses. Future real catalogs must use actual backend content state.

## Files changed
- `MintProfessionalHub.kt`: reusable local nested UI for career navigation, age-neutral copy, progress and unavailable services.
- `MainActivity.kt`: Home entry and existing-route callbacks.

## Constraints / regression
- Keep bottom tabs Home, Games, Clips, Speak, Profile.
- Do not modify `com.speakup.app`, preference file `speakup_learning`, reward IDs, speech permission semantics, cloud backends, AI providers, payment settings or score logic.
- No invented certificates, ratings, course counts or saved interview transcripts.

## Outstanding acceptance
- [ ] Android Gradle unit tests, lint, debug APK and monorepo CI on the final branch SHA
- [ ] Real device Back from nested industry -> Hub, rotation/Activity restoration and accessibility
- [ ] Dark/light/system, 320dp, tablet and 200% font screenshot review
- [ ] Support real persisted industry selection or deep links if product requires them
- [ ] P04–P16 dedicated course/detail/writing/vocabulary/interview/map/certificate UI, tablet adaptation and empty/error components are not delivered until supporting content/contracts exist
- [ ] User approval before merging; no production deployment

This PR is an incremental UI integration, not completed Sprint 5 acceptance.
