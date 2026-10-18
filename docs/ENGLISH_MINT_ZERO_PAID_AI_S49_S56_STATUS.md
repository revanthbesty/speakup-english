# English Mint Sprints 49–56 — evidence and release decision

## Current decision: NO-GO
The current PR stack is a foundational policy/architecture draft. It does not provide a proven fully operational local LLM, offline speech recognizer, device-tested download manager, editable transcript, UI-integrated Mint Buddy, full adapter integration, production release, paid-cloud-free runtime traffic audit or verified Android device acceptance.

| Sprint | Delivered in draft | Still required for original SRS acceptance |
|---|---|---|
| 49 | Manifest and system inventory, no-go audit | SDK graph, device benchmarks, model license sign-off |
| 50 | Local-only fail-closed router and tests | Integration into app; runtime egress capture and CI network-blocking |
| 51 | Speech-consent and verified-offline requirement policy | Working offline STT/TTS Android pathways, permission UX and accuracy matrix |
| 52 | Model approval/checksum/device gate | Pinned approved model and license, runtime inference, download/remove/cancel, thermals |
| 53 | Deterministic multi-level Buddy fallback text | In-app chat UI, safe curated dialogues and child testing |
| 54 | Non-mutating answer/rule adapters | Connect to real grammar/story/professional screens; quality audit |
| 55 | Conservative Lite eligibility policy | Actual device profiling, TalkBack, dynamic type, light/dark and latency data |
| 56 | Fail-closed release evidence gate | End-to-end security/regression/upgrade test, APK, evidence sign-off, rollback |

## Non-negotiable
₹0 paid AI API/token spend. No hidden cloud AI inference fallback. No publishing, merge, deploy, paid product procurement or enabling remotely shared child transcripts. Every new model and voice engine must be evaluated for license, offline ability and device safety independently. Existing achievements, learner progress and answer keys cannot be replaced by machine-generated material.

## Exit checklist
CI and exact-SHA APK must be green. Independently test airplane-mode core learning, model not installed/error/crash, speech consent and deletion, 1000 answer-key invariance tests, merged manifest and traffic egress, licenses, RAM/thermal benchmarks and device accessibility. Explicit owner sign-off is required. Until then leave release disabled.

The AI branch stack is based on unmerged Sprint 45 PR #66. Sprint 46–48 professional work remains unfinished separately. Do not merge or deploy.
