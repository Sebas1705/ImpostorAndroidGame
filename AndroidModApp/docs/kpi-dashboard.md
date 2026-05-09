# KPI Dashboard

Track these indicators monthly to ensure template health improves over time.

## Quality KPIs

- Detekt findings trend (target: down or stable).
- Unit test coverage trend from `coverageUnitTestAll`.
- CI success rate on pull requests.
- Regression escape rate (bugs reported after release).

## Delivery KPIs

- Median pull request merge time.
- Lead time from issue creation to first maintainer response.
- Number of releases per quarter.
- Monthly dependency report reviewed (`dependencyUpdates --no-parallel`).

## Security KPIs

- Time to merge critical dependency updates.
- Open vulnerability count from dependency/security scans.

## Monthly Review Routine

1. Review CI status trends and detekt/coverage deltas.
2. Review dependency report output from monthly workflow artifact.
3. Document regressions and define an owner for corrective actions.
4. Link actions to a milestone or triage board entry.

