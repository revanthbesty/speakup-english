# SpeakUp English

AI-powered speaking-first English learning platform.

## Release

Canonical staging release: **v5.2.5**.

## Verification

Run the complete engineering gate with:

```bash
pnpm verify:release
```

For staging infrastructure:

```bash
pnpm staging:preflight
pnpm staging:smoke
```

See `README_STAGING.md` for the staging deployment sequence.
