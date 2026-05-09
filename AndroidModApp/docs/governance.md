# Governance

Defines ownership, SLAs, and decision rules used to keep this template stable and active.

## Scope

- Applies to `AndroidModApp/` technical decisions and maintenance process.
- Complements repository root governance assets (`CODEOWNERS`, issue forms, PR templates, workflows).

## Roles

- Primary maintainer: release owner, final approver for high-impact changes.
- Backup maintainer: triage and merge backup when primary owner is unavailable.
- Contributors: propose changes through PRs and follow template standards.

## Service Levels

- New issues triaged within 2 business days.
- New pull requests acknowledged within 2 business days.
- Critical security updates prioritized and merged once required checks are green.

## Merge Policy

- At least one maintainer approval.
- Required branch protection checks should include:
  - `Build + Lint`
  - `Detekt`
  - `Unit Tests + Coverage`
  - `Changelog Label Gate`
  - `Dependency Review` (PR context)
- `CODEOWNERS` must be updated when ownership boundaries change.

## Decision Model

- Small, backward-compatible changes: maintainers decide in PR discussion.
- Changes with template contract impact: follow RFC process in `docs/template-evolution.md`.
- Breaking changes: include migration guidance in release notes and changelog.

## Review Cadence

- Weekly: triage and priority review.
- Monthly: release readiness review (quality gates, changelog labels, dependency report).

