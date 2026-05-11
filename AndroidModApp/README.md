# AndroidNativeBase Template

Android multi-module template with centralized Gradle convention plugins, shared quality tooling, CI validation, and AI-ready project context docs.

## What this template gives you

- Build-logic plugins in `build-logic/` to keep module `build.gradle.kts` files small
- Multi-module architecture (`app`, `core`, `data`, `domain`, `feature`)
- Detekt static analysis with shared config in `config/detekt/detekt.yml`
- Coverage aggregation task via `coverageUnitTestAll`
- CI workflow split by responsibility in the repository root `.github/workflows/validate.yml`
- AI context pack (`AGENTS.md`, `CLOUD.md`, `ARCHITECTURE.md`, `MODULE_MAP.md`, `AI_INDEX.md`)

## Quick start

```powershell
.\gradlew.bat tasks
.\gradlew.bat assembleDebug
```

## Quality commands

```powershell
.\gradlew.bat lintDebug
.\gradlew.bat detekt
.\gradlew.bat coverageUnitTestAll
.\gradlew.bat dependencyUpdates --no-parallel
```

## Debug diagnostics

- LeakCanary is enabled in `debug` app builds to detect memory leaks.
- Chucker is enabled in `debug` network stack (module `data:retrofit`) to inspect HTTP traffic on-device.
- A Debug Tools route is available from `Home Face` in debug builds to quickly validate Couchbase + OpenDB runtime data.

## Startup flow

- App startup now routes through `Splash -> Login -> Home` in `feature:main` navigation.
- `Login` uses Google Sign-In + Firebase Authentication.
- `Home` now delegates to `feature:home:main` subnavigation (`face`, `ranking`, `profile`) with bottom navigation.
- `Face` includes offline/online game mode cards and a `Word Bank` entry route in main navigation.
- `Profile` owns the sign-out action for returning to login.
- `Splash` is now minimal (app icon + progress indicator).
- `AppTheme` defaults to the fixed branded palette (dynamic color disabled by default).

## Template docs

- Module standards: `docs/module-standards.md`
- Module checklist: `docs/module-checklist.md`
- Library and tooling audit: `docs/library-audit.md`
- Governance model: `docs/governance.md`
- Triage workflow: `docs/triage-workflow.md`
- KPI dashboard: `docs/kpi-dashboard.md`
- 30-60-90 rollout checklist: `docs/30-60-90-checklist.md`
- Template evolution (RFC): `docs/template-evolution.md`
- Release policy: `docs/release-policy.md`
- Build-logic guidelines: `build-logic/BUILD_LOGIC_BEST_PRACTICES.md`
- New module build script templates: `config/templates/`
- Monthly dependency-report workflow template: `config/templates/dependency-report-monthly.workflow.yml.template`
- Docs links index: `docs/LINKS_INDEX.md`
- Docs merge checklist: `docs/DOCS_CHECKLIST.md`

## AI context docs

- AI reading index by task: `AI_INDEX.md`
- Agent operating contract: `AGENTS.md`
- Cloud and runtime dependencies: `CLOUD.md`
- High-level architecture map: `ARCHITECTURE.md`
- Module-by-module reference: `MODULE_MAP.md`
- Agent execution playbook: `docs/ai/AGENT_PLAYBOOK.md`
- Architecture guardrails for AI changes: `docs/ai/ARCHITECTURE_GUARDRAILS.md`
- Existing data-flow catalog: `docs/ai/FLOW_CATALOG.md`
- Pattern replication recipes: `docs/ai/PATTERN_RECIPES.md`

## Community health files

- Issue intake forms: repository root `.github/ISSUE_TEMPLATE/`
- Pull request template: repository root `.github/PULL_REQUEST_TEMPLATE.md`
- Code ownership: repository root `.github/CODEOWNERS`
- Source-controlled repository labels: repository root `.github/labels.json`
- Label sync workflow: repository root `.github/workflows/label-sync.yml`

## Contributor and release files

- Contributing guide: repository root `CONTRIBUTING.md`
- Security policy: repository root `SECURITY.md`
- Support guide: repository root `SUPPORT.md`
- Changelog: repository root `CHANGELOG.md`
- Release notes categories: repository root `.github/release.yml`
- Release publish workflow: repository root `.github/workflows/release.yml`
- PR changelog label gate: repository root `.github/workflows/changelog-label-gate.yml`
- Release tag helper: repository root `release-tag.ps1`
- PowerShell release aliases: repository root `release-tag-alias.ps1`
- One-shot release wrapper: repository root `new-release.ps1`

Release notes and `CHANGELOG.md` entries are generated automatically from merged PR labels during the release workflow.

Quick alias flow for a one-shot release from PowerShell:

```powershell
Push-Location "C:\Users\sebss\Documents\AndroidNativeBase"
. .\release-tag-alias.ps1
nrelease -Patch 1 -DryRun
Pop-Location
```

## Detekt rollout approach

Detekt uses a gradual tightening policy to avoid blocking early adoption.

- Current gate lives in `config/detekt/detekt.yml` (`build.maxIssues`)
- Reduce the threshold in small PRs as violations are fixed
- Optional module baseline files can be added under `config/detekt/baselines/`

