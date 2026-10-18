# English Mint — UI Sprint 4 audit and acceptance

Base: Sprint 3 PR #19, stacked on still-unmerged Sprint 2 PR #18, Sprint 1 PR #17 and UX rescue PR #16. No merge of branches into main.

## Existing capability audit

| Requested experience | Available service/UI | Sprint 4 result |
| --- | --- | --- |
| Finite swipeable Clips | MintFeedScreen, MintFeedCatalog and offline illustrated scenes, vertical drag, caption frames | Retained; simplified title, controls and saveable level/clip/scene position |
| Subtitles | Authored frame.caption | Toggle retained |
| Play/pause/progress | Timed authored scene transitions, not actual streaming video | Retained; do not call a video player |
| Audio | Existing ListeningAudio for spokenLine | Retained; provider availability depends on device |
| Word sheet | No verified dictionary API or definitions | Contextual sentence sheet with honest missing-definition notice |
| Story quiz | Existing challenge answer and completed challenge keys | Retained unchanged |
| Story conversation | Existing optional onSpeakingPrompt to Speak > lessons | Retained, no AI claims |
| Speech | Existing Android speech recognition and consent callback | Retained; no new microphone request or AI |
| Roleplay | Existing MintGuidedConversation scenario engine | Exposed with clearer label |
| Speaking history | Existing completed guided voice turn keys (no voice or transcript archive) | Truthful count; do not invent individual recording history |
| Real-time AI scoring | No verified AI scoring service | Not implemented or advertised |

## Routes
Home -> Clips tab (`Feed` internal route) -> finite story -> quiz -> optional Speak.
Games -> Stories -> Feed; Android Back returns to Games from this entry path.
Speak -> roleplay conversations OR practice a lesson; device mic consent is requested in context.
Profile and Games continue unchanged.

## Edited source files
- `MintFeedScreen.kt` — new word-context sheet, remembered scene/caption/level, clip copy, accessible horizontally scrollable controls.
- `MainActivity.kt` — Speak landing and truthful saved guided-turn count.

## Required acceptance, currently NOT verified
- [ ] Android test/lint/build/CI on exact branch SHA and APK artifact
- [ ] Clip swipe and Back after level selection; scene resume after Activity recreation
- [ ] Captions, playback and speech provider unavailable/denied cases
- [ ] True microphone consent, no fake transcript or simulated score
- [ ] 320dp, tablet, 200% font, TalkBack, dark/light/system, reduced motion
- [ ] Owner's screenshots of Clips, word sheet, Speak roleplay and denied microphone
- [ ] No XP/history regressions after matching-signed update

## Outstanding non-implemented Sprint 4 board elements
There is no third-party short-video streaming backend, 9:16 real-video viewer, vocabulary dictionary, true online AI speaking partner, real-time pronunciation accuracy score, saved detailed speaking session history, or connected share/comment actions. These must remain explicitly unavailable until supported. No fabricated 85% scores, session timestamps, bookmarks or tokens. Responsive tablet layouts, instrumented UI screenshot tests and full visual board parity remain outstanding.

No production merge, paid API activation or data migration.
