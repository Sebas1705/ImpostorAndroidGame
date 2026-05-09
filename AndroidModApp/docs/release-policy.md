# Release Policy

This template follows a predictable release rhythm to stay active and safe to adopt.

## Cadence

- Weekly: triage issues and pull requests.
- Biweekly: dependency updates and plugin upgrades.
- Monthly: tagged template release (`vYYYY.MM.PATCH`) with migration notes.
- Monthly: generate dependency update report with `dependencyUpdates`.
- Scheduled workflow: repository root `.github/workflows/dependency-report-monthly.yml`.
- Template for reuse: `config/templates/dependency-report-monthly.workflow.yml.template`.

## Branching

- `main`: stable branch for releases.
- `develop`: optional integration branch for larger batches.
- Feature work should use short-lived branches and pull requests.

## Versioning

- `MAJOR`: breaking build-logic or template contract changes.
- `MINOR`: backward-compatible features and new module scaffolds.
- `PATCH`: fixes, docs, dependency refreshes.

Release tags use `vYYYY.MM.PATCH` (example: `v2026.05.1`).

## Release Automation

- Workflow: repository root `.github/workflows/release.yml`
- Label gate workflow: repository root `.github/workflows/changelog-label-gate.yml`
- Release notes categories: repository root `.github/release.yml`
- Automatic publish runs when a matching tag is pushed.
- Manual publish is also available with workflow dispatch using a `version` input.
- `CHANGELOG.md` is updated automatically from merged PR labels before the release is published.

## Tag Helper Script

Use repository root `release-tag.ps1` to create tags with the correct format.

```powershell
cd C:\Users\sebss\Documents\AndroidNativeBase
.\release-tag.ps1 -Patch 1 -DryRun
.\release-tag.ps1 -Patch 1 -Push
```

For a one-shot flow that runs `detekt`, creates the tag, and prints the expected release URL:

```powershell
cd C:\Users\sebss\Documents\AndroidNativeBase
.\new-release.ps1 -Patch 1 -DryRun
.\new-release.ps1 -Patch 1 -Push
```

## Optional PowerShell Aliases

For the current session only:

```powershell
cd C:\Users\sebss\Documents\AndroidNativeBase
. .\release-tag-alias.ps1
rtag -Patch 1 -DryRun
```

For the one-shot flow with `detekt` + tag preview/push:

```powershell
cd C:\Users\sebss\Documents\AndroidNativeBase
. .\release-tag-alias.ps1
nrelease -Patch 1 -DryRun
```

If you want it permanently, add this line manually to your PowerShell profile:

```powershell
. "C:\Users\sebss\Documents\AndroidNativeBase\release-tag-alias.ps1"
```

## Release Checklist

1. Validation workflows green (`build + lint`, `detekt`, `unit tests + coverage`, `security`, `changelog label gate`).
2. Pull requests since the previous release have correct changelog labels (`bug`, `enhancement`, `documentation`, `ci`, `security`, `breaking-change`, or `skip-changelog`).
3. Migration notes included for any breaking/deprecation change.
4. Git tag pushed after merge to `main`.
5. Dependency update report reviewed (`./gradlew dependencyUpdates --no-parallel`).
6. AI context docs reviewed when release includes architectural or operational changes (`AGENTS.md`, `CLOUD.md`, `ARCHITECTURE.md`, `MODULE_MAP.md`, `AI_INDEX.md`).

