# English Mint — Sprint 7 Membership and Billing Safety

## Repository baseline
Sprint 7 extends verified Sprint 6. The Android application currently has no verified Google Play Billing client integration and no demonstrated secure subscription entitlement synchronization. The existing API identity and content modules do not imply a live product catalog or purchase-token verification system.

## Implemented now
- `MintMembershipPolicy.kt`: typed Free/Plus/Pro and future Family membership tiers, explicit feature entitlements, paid feature gates, server-verification and signature requirements, expiry/grace/cancel handling and fail-closed behavior for all unverified or missing grants.
- `MintMembershipScreen.kt`: informative premium membership comparison, reachable from Profile and Home, with a meaningful Free plan and transparent future Plus/Pro options. No fabricated INR/USD prices, no clickable purchase flow, no collection of card details.
- `MainActivity.kt`: non-destructive integration of membership comparison without removing any existing Free games, speech or progress views.
- `MintMembershipPolicyTest.kt`: confirms always-available Free lessons/games/speaking/progress, forged local PRO claims rejected, Plus cannot access Pro features, server-verified grace expiry, cancelled subscriptions fail closed, Family does not accidentally activate, absent/unverified billing products cannot start checkout.

## Proposed live billing architecture (requires approval; deliberately NOT activated)
1. Configure product IDs and region-specific offers in the Google Play Console under the approved merchant account. Use live `ProductDetails` prices directly from Google Play; never hardcode amounts or currencies into the APK.
2. After user approval, add Google Play Billing Library with an injectable client adapter that queries products and starts a purchase through the Play purchase sheet. No app-side direct payment collection.
3. Submit purchase tokens over authenticated TLS to the existing first-party API. Validate purchase state with Google's authoritative APIs from server-side service-account credentials (never bundled in Android), acknowledge according to platform rules, and store only necessary token/account entitlement metadata.
4. The API issues time-bounded signed membership proof with account binding, tier, scopes, expiry and verification state. The Android client uses `MintMembershipPolicy` to enforce feature allowances; premium endpoints must ALSO enforce entitlements on the server. A local preference cannot upgrade a plan.
5. Handle pending purchases, duplicate notifications, cancellations, renewals, account transfers, grace windows, refunds, restore purchases, idempotent webhook replay, time skew and Family plans with separate audited tests.
6. Publish consent/privacy policy and obtain explicit approval before sandbox/live billing, storing tokens, cloud costs or payment enablement.

## Feature policy
Free = basic lessons, curated core games, private speaking, basic progress.
Plus = Free + future expanded game packs, premium adventures and additional live AI allowances.
Pro = Plus + future verified advanced speaking analysis, exam pathways and detailed analytics.
Family = future; no automatic premium entitlement. Prices, AI quotas and content allowances must come from trusted server/config and Play product metadata, not hardcoded into application screens.

## Verification and limitations
Tests should pass `testDebugUnitTest`, Android `lintDebug`, `assembleDebug`, monorepo CI and release gates. The Play developer account, credentials, merchant settings, purchase verification service, server-signed entitlement issuer and premium backend content/AI do not exist as verified Sprint 7 deliverables. Mark live billing, restore, purchase activation, Family management and Pro-grade AI as **blocked pending external approval/integration**, not complete. Device testing remains deferred to Sprint 10.

## Migration
Keep `com.speakup.app`, existing `speakup_learning` preferences, XP, game IDs, audio choice and achievements unchanged. No premium flag is persisted to local preferences. No user data deleted, migrated to paid services or posted externally.

## Sprint exit
Android and monorepo checks green; reviewable stacked PR with all blockers documented. Do not merge into protected main or publish without authorization.
