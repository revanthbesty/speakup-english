# English Mint Sprint 46 — Editorial CMS and publishing evidence

**Decision: staging only; no content is published.** The source-aware approval model requires a unique item ID, positive version, origin, rights reference, human reviewer, traceable approval reference and explicit approval. Missing or duplicate approval must block publishing. The four-answer pilot and Professional English exercises are NOT marked editorially approved.

Not yet implemented: authenticated author/reviewer accounts, immutable moderation history, CMS backend, revision/version rollback, data import, approval UI and production publishing. These features need security review and backend design before enabling shared editorial operations.

QA: `MintProfessionalPublishingTest` covers an unapproved draft and duplicate/missing item approvals. Confirm there are no write-to-server publication code paths. Run exact-head CI, Android build, and human review before marking complete. Keep existing app and saved progress untouched.

No merge, release, public publishing or deployment.