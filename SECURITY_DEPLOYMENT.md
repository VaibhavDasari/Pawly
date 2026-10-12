# Pawly security and deployment notes

## Required configuration

Spring reads configuration from the process environment. It does not load `.env` files automatically. Copy `backend/.env.example` values into your shell or deployment secret manager; replace every placeholder. Generate `JWT_SECRET` from at least 32 random bytes (for example, `openssl rand -base64 48`). Never use the example value. The secret is rejected at startup if it is absent, too short, or resembles a placeholder.

For local development, configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`; use a dedicated MySQL account with only the permissions Pawly needs. Set `DDL_AUTO=update` only for local development. In production set `SPRING_PROFILES_ACTIVE=prod`, use `DDL_AUTO=validate` (the profile enforces it), and provide `CORS_ALLOWED_ORIGINS` as a comma-separated list of exact HTTPS frontend origins. Production startup requires database and CORS environment values. `VITE_API_BASE_URL` is public frontend configuration and must never contain a secret.

The login limiter allows 10 requests per source socket IP per 15 minutes; registration allows 5 per source socket IP per hour. Excess requests receive 429 and `Retry-After`. State is in memory, resets on process restart, and is not shared across instances. It intentionally ignores `X-Forwarded-For`; if deployed behind a proxy, configure the platform to rate limit at the edge and do not make the application trust client-supplied forwarding headers.

## HTTPS, database, and operations

Terminate TLS at the hosting platform and force HTTPS there. HSTS is emitted by the API when the request is marked secure; configure the trusted hosting proxy to preserve the secure request state, or set HSTS at the edge. Keep MySQL private to the application network, use a least-privilege account, and restrict inbound database access. Production schema mode validates rather than auto-altering tables. Back up the database before migrations and on a regular schedule; periodically test restoring a backup to a separate database.

Production SQL statement logging is disabled. API errors do not include stack traces or binding internals. Keep application access logs free of Authorization headers and request bodies, and avoid logging personal health notes. The React app stores its bearer JWT in localStorage so it can restore a session after refresh. Any successful same-origin script execution can read that token; keep dependencies current, deploy a restrictive Content Security Policy at the frontend host, and avoid injecting untrusted HTML.

## Credential exposure and history

The checked-in application configuration previously contained a database password and a JWT signing secret. Treat both as exposed and rotate them before deployment, including if they were intended only for development. This workspace has no `.git` metadata, so tracked-file status and commit history could not be checked. Removing credentials from the current files does not remove them from Git history. If the project has a remote repository, inspect its history and rotate any credentials found; do not rewrite shared history without a coordinated repository plan.

## Dependency review

Run `npm audit` from `frontend` and `mvnw.cmd org.owasp:dependency-check-maven:check` from `backend` in an environment with access to the vulnerability databases. Findings should be reviewed before targeted dependency updates; do not blindly upgrade the complete dependency tree.
