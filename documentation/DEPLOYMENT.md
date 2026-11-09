# NEQUARD deployment guide

## Service ownership

Use a single production owner for each stateful service. A practical initial split is:

| Component | Hosting target | Notes |
|---|---|---|
| Spring Boot API | Railway **or** Render | Choose one production API instance. |
| PostgreSQL | Same provider as API | Use managed PostgreSQL and private networking where available. |
| React static frontend | Render Static Site | Build from `frontend/`; configure `VITE_API_URL` at build time. |
| FastAPI ML service | Render Web Service or Railway | Private/internal access from API where supported. |
| Git repository and CI | GitHub | CI validates code; deploy only after green checks. |

Do not provision two production databases or run duplicate production backends without an explicit failover design.

## Required secrets and variables

Backend:

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `POSTGRES_USER`, `POSTGRES_PASSWORD`
- `JWT_SECRET` — unique random secret, at least 32 bytes; never use the example value.
- `ML_SERVICE_URL` — internal service URL when the ML service is deployed.
- `PORT` — supplied by the host when applicable.

Frontend build:

- `VITE_API_URL` — public HTTPS base URL of the Spring Boot API, e.g. `https://<your-api-host>`.

ML service:

- Configure an internal-only service where supported. Do not expose administrative or infrastructure-control endpoints publicly.

## Render

1. Connect the GitHub repository to Render and review `render.yaml` before applying it.
2. Confirm the region, instance plans, database plan, service names and estimated cost in the Render dashboard.
3. Provision PostgreSQL and verify its connection variables are mapped to the backend.
4. Set a strong `JWT_SECRET` in the backend service environment.
5. Set `VITE_API_URL` on the static frontend service to the deployed backend HTTPS URL, then trigger a frontend rebuild.
6. Verify `/api/v1/health`, ML `/health`, CORS, database migrations and browser network requests.

## Railway

1. Create a Railway project from the GitHub repository.
2. Deploy the backend using `backend/Dockerfile` and set the service root/build configuration accordingly.
3. Add a managed PostgreSQL service and map its connection values to the backend variables above.
4. Configure `JWT_SECRET`, `ML_SERVICE_URL`, health-check path `/api/v1/health`, and the public domain only if the API must be internet-accessible.
5. Deploy the frontend and ML service only if you have intentionally assigned them to Railway; otherwise keep them on their selected host.
6. Confirm deployment logs, health checks, migrations, CORS and end-to-end API calls.

## Release checklist

- [ ] GitHub Actions pass on the release commit.
- [ ] Production secrets are configured and absent from Git history.
- [ ] Database backups and restore procedure are defined.
- [ ] HTTPS and CORS allow only the intended frontend origin.
- [ ] Health endpoints report healthy dependencies, not merely process uptime.
- [ ] A real account and role-permission matrix are tested.
- [ ] No demo/simulated metrics are displayed as live telemetry.
- [ ] Rollback procedure and deployment owner are documented.

Deployment is not considered verified until these checks are performed against the actual hosted services.
