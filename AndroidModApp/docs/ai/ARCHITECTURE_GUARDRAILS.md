# Architecture Guardrails

Rules to keep the module architecture consistent.

## Allowed Dependencies

- `app` -> `feature`, `domain`, `core`
- `feature` -> `domain`, `core`
- `domain` -> `core`, repository interfaces/models/mappers
- `data` -> `domain` abstractions and `core` utilities

## Forbidden Dependencies

- `feature` -> `data` implementations
- `domain` -> `feature`
- `core` -> `feature` or `data`

## Data Flow Contract

- UI calls ViewModel in `feature`.
- ViewModel uses use case in `domain`.
- Use case talks to repository interface.
- `data:repositories` binds interface to implementation.
- Implementation delegates to data source (`retrofit`, `couchbase`, etc.).

## DI Contract

- Bind interfaces in `data/repositories/.../RepositoriesDataModule.kt`.
- Keep networking setup in `data:retrofit` modules.
- Keep variant-only wiring in `src/debug` or `src/release`.

## Debug Tooling Contract

- Debug-only dependencies via `debugImplementation`.
- Provide release-safe alternatives when needed (`releaseImplementation` no-op).

