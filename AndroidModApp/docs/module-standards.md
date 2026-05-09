# Module Standards

Use these standards when creating or refactoring modules in this template.

## Layer intent

- `core`: reusable framework-like shared building blocks
- `data`: external/local data source implementations and repositories
- `domain`: business models, use cases, service interfaces
- `feature`: UI flows and screen-level orchestration

## Build logic usage

- Apply one primary convention plugin (`buildlogic.android.core`, `buildlogic.android.data`, etc.)
- Keep module-level Gradle files minimal and dependency-focused
- Avoid hardcoded versions in module scripts; use `gradle/libs.versions.toml`
- Prefer variant-aware dependencies for debug-only tooling (`debugImplementation` / `releaseImplementation`)

## Naming and package conventions

- Use lowercase module folders and clear bounded context names
- Keep package names aligned with module intent
- Keep public APIs in `domain`; avoid leaking `data` implementation details

## Testing standards

- Unit tests for business logic are required in `domain` and `data`
- Keep test names behavior-oriented (`given_when_then` style is acceptable)
- Prefer deterministic tests (no network, no clock dependence without fakes)
- Use shared testing dependencies from `buildlogic.android.unit.test`
- If a module exposes diagnostics flows, include at least one smoke test path (compile + detekt)

## Static analysis standards

- Detekt is required for all modules via convention plugin
- Fix new findings in touched files before merging
- Use baselines only for legacy findings and reduce them over time

## CI expectations

- `build + lint`, `detekt`, and `unit tests + coverage` must pass
- Attach report artifacts for failed jobs to speed up debugging

## Documentation expectations

- Update related docs when architecture/module boundaries change.
- Keep module references consistent with `MODULE_MAP.md` and `AI_INDEX.md`.

