# Environment Matrix

| Setting | Local | Staging | Production |
|---|---|---|---|
| NODE_ENV | development | production | production |
| Database | local PostgreSQL | managed PostgreSQL | managed PostgreSQL |
| AI provider | mock/default | controlled real provider | approved real provider |
| Speech providers | mock/default | controlled real provider | approved real provider |
| Logging | console | structured | structured + centralized |
| Backups | optional | required | required + tested |
| HTTPS | optional local | required | required |
| Secrets | local env | secret manager | secret manager |
