# Triage Workflow

A lightweight triage loop keeps backlog healthy and contributor response times predictable.

## Label System

- `bug`: broken behavior in template defaults.
- `enhancement`: improvement request.
- `template`: module scaffold or build-logic evolution.
- `documentation`: docs-only updates.
- `good first issue`: suitable for new contributors.
- `priority/critical`, `priority/high`, `priority/normal`.
- `breaking-change`: highlights releases that require migration notes.
- `skip-changelog`: excludes a pull request from generated release notes and changelog sections.

Label definitions are versioned in repository root `.github/labels.json` and can be synchronized with repository root `.github/workflows/label-sync.yml`.

Release automation dependencies:

- Labels drive generated release notes and `CHANGELOG.md` categorization.
- Pull requests are blocked by `.github/workflows/changelog-label-gate.yml` if no changelog label is present.

## Weekly Issue Triage

1. Confirm reproducibility and expected behavior.
2. Apply labels and priority.
3. Assign owner and target milestone.
4. Close stale duplicates with references.

## Pull Request Triage

1. Confirm scope and links to issue/RFC.
2. Ensure CI checks pass.
3. Verify changelog label exists (`bug`, `enhancement`, `documentation`, `ci`, `security`, `breaking-change`, or `skip-changelog`).
4. Request changes or approve within SLA.
5. Merge with squash and clear commit title.

## Escalation Rules

- `priority/critical`: same-day maintainer review target.
- Security-related items: fast-track path and coordinated release when needed.

## CI Triage Notes

- `Security` uses CodeQL manual build with `:app:assembleDebug` to avoid release-only task failures.
- `Security` and `Validation Pipeline` use Gradle cache setup, so the first run after cache invalidation can be slower than subsequent runs.
- If `Security` exceeds timeout, capture failed logs and check whether runtime is spent in `Analyze` (CodeQL extraction) or Gradle build steps.
- `Validation Pipeline` and `Security` now detect changed paths and skip heavy jobs when `AndroidModApp/**` is not affected.
- `Detekt` runs with Gradle configuration cache enabled as a controlled rollout; if regressions appear, disable `--configuration-cache` first.
- If repository Code Scanning is disabled, `Security` skips CodeQL analyze upload and emits a warning instead of failing.

