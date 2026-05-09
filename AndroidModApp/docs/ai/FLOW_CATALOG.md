# Flow Catalog

Catalog of existing end-to-end flows to replicate safely.

## Flow 1: Couchbase Read/Insert from MVVM Sample

- Entry: `feature/mvvmsample/.../MvvmSampleViewModel.kt`
- Domain:
  - `domain/usescases/settings/.../GetMyDocsUseCase.kt`
  - `domain/usescases/settings/.../InsertDemoMyDocUseCase.kt`
- Repository binding:
  - Interface `IMyDocRepository`
  - Bind in `data/repositories/.../RepositoriesDataModule.kt`
- Repository implementation:
  - `data/repositories/.../MyDocRepository.kt`
- Data source:
  - `data/couchbase/.../MyDocCBDataSource.kt`

Replication notes:

1. Add/extend use case in `domain`.
2. Reuse repository interface when possible.
3. Keep storage specifics in `data` only.
4. Return UI-ready state from ViewModel, not from use case.

## Flow 2: Debug Tools Composite Diagnostics (Couchbase + OpenDB)

- Entry: `feature/main/.../debug/DebugToolsViewModel.kt`
- Domain:
  - `GetMyDocsUseCase`
  - `domain/usescases/opendb/.../GetTriviaTenQuestionsUseCase.kt`
- Repositories:
  - `MyDocRepository` for Couchbase path
  - `OpendbRepository` for network path
- Data sources:
  - `MyDocCBDataSource`
  - `data/retrofit/.../OpendbApiDataSource.kt`

Replication notes:

1. Aggregate multiple use cases in ViewModel for diagnostics only.
2. Keep error handling in ViewModel state.
3. Do not bypass use cases from `feature` to `data`.

## Flow 3: App Startup Navigation (MVI)

- Entry: `feature/main/.../AppNav.kt`
- ViewModel: `feature/main/.../MainViewModel.kt`
- Base pattern: `core/common/.../MVIBaseViewModel.kt`

Replication notes:

1. Model events as intents.
2. Keep navigation trigger in feature state transitions.
3. Avoid direct side effects from composables.

