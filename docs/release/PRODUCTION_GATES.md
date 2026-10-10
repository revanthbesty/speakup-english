# Production Gates

A release is **GO** only when all applicable gates pass.

| Gate | Required | Status in this package |
|---|---|---|
| Repository/package preflight | Yes | Automated script included |
| Migration inventory | Yes | Automated script included |
| Unit/integration tests | Yes | Must execute in installed dependency environment |
| Typecheck/build | Yes | Must execute in installed dependency environment |
| Browser E2E | Yes | Must execute against deployed staging |
| Android validation | Yes | Must execute on supported test devices |
| AI provider validation | Yes | Requires real provider credentials |
| STT/TTS validation | Yes | Requires real speech providers |
| Security verification | Yes | Must execute against staging/production configuration |
| Backup/restore drill | Yes | Must execute against provisioned infrastructure |
| Production smoke test | Yes | Must execute after deployment |
