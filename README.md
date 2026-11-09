# NEQUARD

**NEQUARD — Intelligent Community Network, Connectivity, Security & Digital Infrastructure Platform**  
**Creator:** Jayden  
**Tagline:** Connect. Share. Protect. Empower.

NEQUARD is a modular platform for community connectivity operations, network inventory and monitoring, incident and security workflows, AI-assisted diagnostics, and resilient digital services. It is designed for communities and infrastructure operators working in both connected and constrained environments.

## Repository status

This repository contains an active implementation foundation, not a claim that every proposed capability is production-ready. See [Implementation status](documentation/PHASES.md) for verified foundations and dependencies. Physical network integrations, external provider services, and production ML training require corresponding equipment, datasets, credentials, and validation.

## Architecture

- **Backend:** Java 21, Spring Boot 3, Spring Security, JWT, Spring Data JPA, PostgreSQL, Flyway, Actuator.
- **Frontend:** React, TypeScript and Vite. The UI consumes the versioned backend API; set `VITE_API_URL` at build time.
- **ML service:** Python, FastAPI and statistical/baseline diagnostic endpoints. Baseline heuristics are not trained predictive models.
- **Data:** PostgreSQL with versioned Flyway migrations.
- **Local runtime:** Docker Compose.
- **CI:** GitHub Actions for backend tests, frontend build and ML service tests.
- **Hosting:** Render blueprint is provided. Railway can host the API and a managed PostgreSQL service; deploy only after configuring secrets and database variables.

## Repository layout

```text
backend/          Spring Boot API and Flyway migrations
frontend/         React + TypeScript application
ml-service/       FastAPI diagnostic service
documentation/    Architecture, deployment and implementation status
.github/workflows CI workflows
Dockerfile        Root backend image (Render blueprint)
docker-compose.yml Local development services
render.yaml       Render Blueprint
```

## Local development

Requirements: Java 21, Maven 3.9+, Node.js 20+, Python 3.11+, and Docker Compose.

1. Copy `.env.example` to `.env` and replace every placeholder for your environment.
2. Start dependencies with `docker compose up --build`.
3. Frontend: `cd frontend && npm ci && VITE_API_URL=http://localhost:8080 npm run dev`.
4. Backend tests: `cd backend && mvn test`.
5. ML service: `cd ml-service && pip install -r requirements.txt && uvicorn app.main:app --host 0.0.0.0 --port 8000`.
6. ML tests: `cd ml-service && PYTHONPATH=. pytest -q`.

The frontend currently expects the API at `VITE_API_URL`; it falls back to localhost for local development. Do not use that fallback in a deployed frontend.

## Deployment

Read [Deployment guide](documentation/DEPLOYMENT.md). Keep the API, ML service and frontend URLs in environment configuration. Never commit credentials. Set a unique, high-entropy `JWT_SECRET` (at least 32 bytes) and configure database access before exposing the service publicly.

## Integrations

- **GitHub:** source control and CI/CD.
- **Render / Railway:** alternative or complementary hosting targets with one clearly assigned owner per service. Avoid running duplicate production databases.
- **AnyWhereMap:** optional operator-facing map workflow for locating community infrastructure. Use only explicitly consented device/community coordinates; map integration is not yet represented as a live backend integration in this repository.
- **Canva:** create architecture diagrams, project presentation and stakeholder materials from the documented architecture; Canva assets are not runtime dependencies.
- **Product Design:** UX research and design review should be performed in Work mode; this chat session cannot execute the Product Design workflow.
- **TickerLayer / Skyscanner:** not core dependencies. Add only behind separately scoped, consented integrations if a defined product requirement emerges.

## Security and data integrity

- Keep secrets in the hosting provider's secret/environment manager.
- Enforce authorization in the backend, not only in the UI.
- Treat simulated telemetry and heuristic ML outputs as such; never label them as live measurements or trained-model predictions.
- Restrict automation to allow-listed actions, explicit permissions, audit records and bounded retries.
- Collect only location and community data required for an operational purpose, with notice and appropriate access controls.

## License

Add a license after selecting the intended distribution terms.
