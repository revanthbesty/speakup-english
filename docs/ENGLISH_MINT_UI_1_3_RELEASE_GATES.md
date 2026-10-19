# English Mint — UI Sprints 1–3: mandatory completion and acceptance gates

Status: **NOT COMPLETE / DO NOT MERGE**. Baseline review: 19 October 2026.
Source of truth: owner's original approved English Mint UI Sprint 1–6 visual boards and handoffs. A successful CI / Android APK build is only a functional prerequisite, not visual sign-off.

## Sprint 1 — Onboarding (Welcome / Age / Goal / Ready)
- [x] Four Compose onboarding steps implemented in `MintWelcomeFlow.kt`
- [x] App owns age band, goal, name and optional placement persistence in `MainActivity.kt`
- [ ] Replace emoji placeholder mascot/activity art with approved original illustrations/assets, not approximations
- [ ] Pixel/layout comparison for **each of four screens** against the approved board
- [ ] Verify first install, Continue/Back/Skip, device rotation, saved state, optional level check, and young-learner copy
- [ ] Test small devices (320/360/412 dp), 200% font, system Back, landscape, TalkBack, dark and light
- [ ] Owner sign-off for four screen screenshots

## Sprint 2 — Home (Today / Learning Path / Explore)
- [x] Existing Today screen has daily quest CTA, Continue Learning, route tiles and recorded learning progress
- [ ] Compare header, visual illustration, primary CTA, cards, hierarchy and spacing directly with Sprint 2 board; undo any invented substitutes
- [ ] Test every Home tile and expanded section, back stack, restored profile and empty/complete learning states
- [ ] Verify actual lesson counts, XP/rewards ledger and dark/light layout on device
- [ ] Owner screenshot approval

## Sprint 3 — Games Hub / Grammar Arena
- [x] Existing hub links to available game modes; Grammar has A/B/C answer choices, check feedback, explanations, and progress
- [ ] Compare Games Hub, selected-game view, Grammar question, feedback and completion against original Sprint 3 reference images
- [ ] Replace emoji/icon substitutes and generic cards with the approved artwork and dimensions
- [ ] Test all game routes, return-to-hub, tab switch, Android Back, grammar answer correction, next/set completion, bookmark/review
- [ ] Verify no duplicate XP/achievement awards on replay and no invented content counts
- [ ] Check grammar accessibility, 200% font, keyboard/gesture and API 29/35 device screens
- [ ] Owner screenshots and approval

## Required evidence before declaring ANY sprint complete
1. Exact Git commit SHA, Android CI + debug APK verification for that SHA.
2. Approved reference and actual Android screenshots paired for every screen, in light and dark modes.
3. Per-route functional checklist, device and accessibility results, recorded defects and fixes.
4. Explicit owner sign-off on matched visuals, then review merge authorization separately.

## Known limitations / parallel work
The original approved boards may contain assets that have not been delivered as repository files. Do not silently replace missing original artwork with generated or emoji substitutes. Current PR #94 remains **draft**, unmerged and undeployed. Workstreams may proceed in parallel, but each gate must remain independently unmet until evidence is attached.