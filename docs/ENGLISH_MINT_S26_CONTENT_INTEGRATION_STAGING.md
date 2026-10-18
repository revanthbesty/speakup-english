# English Mint Sprint 26 ↔ Activity Content Integration Staging

## Branch relationship
- Sprint 26 implementation base: `feature/english-mint-s26-family-teacher-pilot` (local-only Teacher/Family Studio).
- Candidate content drafts: `feature/english-mint-4000-activity-content-foundation` (draft PR #35, **288** repair candidates at the time this bridge was created).
- Branches are divergent, not automatically merged. Do not merge PR #35 into Sprint 26 or directly bundle those drafts.

## What this integration actually adds
- `MintFamilyContentGate`: a pure Kotlin, fail-closed boundary for future *offline approved* content activity IDs.
- Word Builder and Reading are the only eligible content categories because those are the existing Sprint 26 family/teacher sample game routes.
- Checks nonblank ID, supported CEFR level, verified source and **independent editorial approval**.
- `shippedContentCandidates()` intentionally returns no records. All PR #35 activity samples are unapproved, and none is made live or assigned.
- Unit tests check unapproved material stays inaccessible, unsupported categories/levels are denied and only verified/approved records are selected.

## What is **not** integrated or claimed
- The 288 draft JSON records are **not** copied, approved, imported, visible in Sprint 26, or attached to learner progress.
- A self-declared `review_status` in a JSON file does not constitute independent editorial approval.
- Assignment completion remains evidence-gated in existing Sprint 26 code; no Bloom/reward mutation.
- No account, roster, guardian relationship, child profile, shared progress, backend, notifications or external network.
- This is an integration interface and test slice, not a completed runtime content import.

## Future safe content integration gate
1. Complete educational review (CEFR, answers, naturalness, distinctness, age suitability, passage length) and record externally verified approval.
2. Reconcile stable IDs against the source 4,000-record workbook and verify each replacement's category and level.
3. Produce a deterministic *approved-only* offline content artifact; no dynamic paid API.
4. Wire approved IDs into the existing `Games > Word Builder` and `Games > Reading` screens without duplicated rewards, preserving progress.
5. Run Kotlin tests, Android build/lint, device UI/TTS and accessibility acceptance.
6. Real family/teacher multi-user use remains gated by identity/consent/security work from Sprint 26 SRS.

No merge or deploy in this branch.
