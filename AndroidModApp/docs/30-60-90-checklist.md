# 30-60-90 Day Checklist

## First 30 Days

- Enable required branch checks in GitHub using workflow job names.
- Enable Dependabot auto-updates for Gradle and Actions.
- Stabilize CI runtime and artifact visibility.
- Publish and review AI context docs (`AI_INDEX.md`, `AGENTS.md`, `CLOUD.md`).

## Days 31-60

- Reduce `build.maxIssues` in `config/detekt/detekt.yml` by a safe increment.
- Close top recurring lint/detekt findings in touched modules.
- Publish first maintenance release notes.
- Run monthly dependency report and create upgrade issues for high-value updates.

## Days 61-90

- Review KPI trend and tune quality gates.
- Remove or shrink legacy baselines where feasible.
- Plan next quarter roadmap for template evolution and migration support.
- Re-validate AI context docs so they still match module graph and cloud setup.

