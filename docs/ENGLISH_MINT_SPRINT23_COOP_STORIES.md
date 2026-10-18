# English Mint Sprint 23 — Co-op Stories MVP (safe offline prototype)

## Source and intent
Recovered English Mint Sprint 19–28 SRS: Sprint 23 proposes **Co-op Stories MVP**, private asynchronous collaborative story turns with child-safe gating and abuse tests. This initial implementation is deliberately restricted to **pass-the-device / single-device alternating roles** and **does not satisfy** private multi-account async collaboration, room invitations, or network moderation requirements.

## Implemented
- Games > Co-op Stories: original three-scene library story, Explorer and Helper alternating roles, three A/B/C choices and role-specific explanations.
- Incorrect choices do not advance the episode; a completed, validated sequence becomes eligible for a once-only **Story Flower**.
- A saved private completion flag survives restarts and contributes to the existing Home Mint Garden Story Flower count.
- Restart story and return-to-Garden navigation.
- Offline only, no logins, strangers, participant IDs, child-to-stranger chat, microphone recordings, external services, messaging, contact access or invitations.
- Pure Kotlin tests cover scene/option validity, turn alternation, invalid choices and tampered completion.

## Not implemented — critical original exit-gate gaps
- Truly async separate-person turns, private rooms, invitations and secure account identities.
- Child/parent identity and consent model, safe moderation, mute/block/report, audit logs and abuse tests.
- Multi-device sync, role access checks, room expiry, moderation escalation, deletion and privacy controls.
- Device UI / accessibility QA; CI and Android build must be checked against exact PR head.
- Do not enable social/remote co-op before separate child-safety architecture and review.

## Device acceptance
1. Games > Co-op Stories; play both roles on one device; verify clear roles and prompts.
2. Wrong answer shows recovery; it must not advance progress.
3. Correct answers lead through three turns. Collect Story Flower once; replay cannot award again.
4. Restart app; previously collected Story Flower remains in Garden.
5. Test TalkBack/large font, reduced motion, dark/light, age profiles and back navigation.

No merge, no deploy, no paid API or live social service activation.
