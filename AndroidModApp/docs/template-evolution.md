# Template Evolution

Use this process for meaningful template changes so teams can adopt updates safely.

## When to Open an RFC

Open a lightweight RFC pull request when changing:

- Convention plugin behavior in `build-logic/`.
- Dependency versions strategy in `gradle/libs.versions.toml`.
- Module templates in `config/templates/`.
- Quality gates that can break consumer repositories.

## RFC Format

1. Goal and scope.
2. Impacted modules/plugins.
3. Backward-compatibility assessment.
4. Rollout and rollback steps.
5. Validation evidence (CI jobs + local command output).
6. Documentation sync plan (`README.md`, `docs/*`, AI context docs when applicable).

## Deprecation Policy

- Announce deprecations in one release before removal.
- Include replacement guidance.
- Remove only after the next monthly release unless a security fix requires earlier action.

## Required Documentation Sync

- If module boundaries change, update `MODULE_MAP.md` and `ARCHITECTURE.md`.
- If operational workflow changes, update `AGENTS.md` and `AI_INDEX.md`.
- If runtime services or secrets model changes, update `CLOUD.md`.

