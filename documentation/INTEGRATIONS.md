# NEQUARD external integration boundaries

## GitHub
Central source repository, pull requests, issue tracking and CI workflows. Protect the default branch and require successful CI before merging.

## Render and Railway
Hosting providers, not application dependencies. Assign one owner to each production service. Keep secrets in provider environment settings, use HTTPS, configure health checks and avoid duplicate stateful resources.

## AnyWhereMap
Planned operator map experience for community assets and network locations. Before enabling, define the API contract, coordinate precision, user consent, retention, role-based visibility and offline behavior. No live map connector is claimed in this release.

## Canva
Use for stakeholder-facing diagrams and presentations generated from the architecture documentation. Canva is a content-production workflow, not a runtime service.

## Product Design
Use the Product Design workflow in Work mode for UX research, ideation and design QA. It is not executable from standard chat mode.

## TickerLayer and Skyscanner
Not part of the core network-management domain. Any future integration must have a documented use case, user consent, data-minimization review and separate failure handling. Neither service is required for normal NEQUARD operation.
