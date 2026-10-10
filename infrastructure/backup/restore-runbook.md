# Backup and Restore Runbook

1. Take encrypted PostgreSQL backups on the production schedule.
2. Verify backup completion and retention policy.
3. Perform periodic restore drills in an isolated environment.
4. Validate schema version and row counts after restore.
5. Run health/readiness and learner smoke checks.
6. Record restore duration and recovery point.

Never store database credentials in source control.
