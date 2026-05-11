# Flow Catalog

Catalog of existing end-to-end flows to replicate safely.

## Flow 1: Couchbase Read/Insert from Debug Tools

- Entry: `feature/debug/.../DebugToolsViewModel.kt`
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

- Entry: `feature/debug/.../DebugToolsViewModel.kt`
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

## Flow 4: Startup Auth Gate (Splash -> Login -> Home)

- Entry: `feature/main/.../AppNav.kt`
- Startup ViewModel: `feature/main/.../MainViewModel.kt`
- Login flow:
  - `feature/login/.../LoginViewModel.kt`
  - `domain/usescases/authentication/.../SignInWithGoogleUseCase.kt`
  - `data/repositories/.../IAuthenticationRepository.kt`
- Home flow:
  - `feature/home/main/.../HomeMainScreen.kt`
  - `feature/home/face/.../FaceViewModel.kt`
  - `feature/home/face/nav/.../HomeDesign.kt`
  - `feature/home/ranking/.../RankingScreen.kt`
  - `feature/home/profile/.../ProfileScreen.kt`
  - `feature/home/face/categories/.../CategoriesScreen.kt`
  - `feature/home/face/users/.../UserScreen.kt`
  - `domain/usescases/authentication/.../SignOutUseCase.kt`

Replication notes:

1. Keep auth gating in `feature:main` navigation state transitions.
2. Drive login/home actions through domain use cases.
3. Keep app navigation in `feature:main`, including word-bank routing, and tab subnavigation in `feature:home:main`.

## Flow 5: Global Settings Dialog Overlay

- Entry: `feature/main/.../AppNav.kt`
- Screen/UI: `feature/settings/.../AppSettingsDialog.kt`
- ViewModel: `feature/settings/.../AppSettingsViewModel.kt`
- Domain:
  - `domain/usescases/settings/.../ReadSettingsUseCase.kt`
  - `domain/usescases/settings/.../UpdateSettingsUseCase.kt`
  - `domain/usescases/authentication/.../SignOutUseCase.kt`

Replication notes:

1. Keep the full-screen dialog trigger in `feature:main` so it is available from every destination.
2. Keep persistence logic in `feature:settings` ViewModel using settings use cases.
3. Keep sign-out domain-driven and return navigation intent from UI state.
4. Apply locale from persisted `appLanguage` in `feature:main` so UI language and content language stay aligned.
5. On first run, bootstrap `appLanguage` from system locale (`es` devices -> Spanish, otherwise English).

## Flow 6: Categories Selection Persistence

- Entry: `feature/home/face/categories/.../CategoriesScreen.kt`
- ViewModel: `feature/home/face/categories/.../CategoriesViewModel.kt`
- Domain:
  - `domain/usescases/game/.../ReadGameUseCase.kt`
  - `domain/usescases/game/.../UpdateGameSelectedCategoriesUseCase.kt`
- Data persistence:
  - `data/datastore/.../GamePreferencesDataSource.kt`
  - `data/datastore/src/main/proto/GamePreferences.proto`

Replication notes:

1. Open categories as a full-screen dialog hosted from `feature/home/face/nav`.
2. Persist selected category identifiers through game use cases.
3. Treat storage format as implementation detail (`List<String>` in datastore, enum mapping in mappers).

## Flow 7: Game Setup (Players + Mode) Persistence

- Entry: `feature/home/face/users/.../UserScreen.kt`
- ViewModel: `feature/home/face/users/.../UserViewModel.kt`
- Domain:
  - `domain/usescases/game/.../ReadGameUseCase.kt`
  - `domain/usescases/game/.../UpdateGamePlayersUseCase.kt`
- Data persistence:
  - `data/datastore/.../GamePreferencesDataSource.kt`
  - `data/datastore/src/main/proto/GamePreferences.proto`

Replication notes:

1. Open users editor as a full-screen dialog hosted from `feature/home/face/nav`.
2. Persist names as a simple ordered `List<String>` in game preferences.
3. Keep categories, players, mode, and impostor count under the same game persistence model.
4. Keep post-game impostor visibility as part of mode settings and apply it on result rendering.

## Flow 8: Localized Game Words (Couchbase by Language)

- Entry: `domain/usescases/game/.../SearchGameWordsUseCase.kt`
- JSON import entry: `domain/usescases/game/.../ImportDefaultGameWordsUseCase.kt`
- Settings source:
  - `data/datastore/.../SettingsPreferencesDataSource.kt`
  - `feature/settings/.../AppSettingsDialogDesign.kt`
- File source:
  - `data/files/.../MyJsonFileDataSource.kt`
  - `data/files/src/main/assets/*.json`
- Word persistence:
  - `data/couchbase/.../GameWordEntryDoc.kt`
  - `data/couchbase/.../GameWordEntryCBDataSource.kt`
  - `data/repositories/.../GameWordRepository.kt`

Replication notes:

1. Keep one Couchbase collection per language (`es`, `en`) for game word entries.
2. Resolve active language from settings preference before querying words.
3. Store clues as a fixed-size string list (5 items expected by JSON schema).
4. Keep category mapping resilient when converting persistence data into domain models.

## Flow 9: Offline Round Runtime (Reveal -> Discussion -> Result)

- Entry: `feature/home/face/nav/.../design/HomeDesign.kt`
- Runtime dialog: `feature/home/face/nav/.../offline/OfflineGameScreen.kt`
- ViewModel: `feature/home/face/nav/.../offline/viewmodel/OfflineGameViewModel.kt`
- Domain:
  - `domain/usescases/game/.../SearchGameWordsUseCase.kt`

Replication notes:

1. Start the offline round from Face setup only after players/categories are configured.
2. Resolve one random word from selected categories before reveal phase starts.
3. Keep per-player reveal state isolated from discussion/voting state.
4. End round when all impostors are voted out, impostors force win, tie condition is met, or impostors guess the word.

## Flow 10: Ranking Split (Online Placeholder + Offline Collection)

- Entry: `feature/home/ranking/.../RankingScreen.kt`
- ViewModel: `feature/home/ranking/.../viewmodel/RankingViewModel.kt`
- Runtime writer:
  - `feature/home/face/nav/.../offline/viewmodel/OfflineGameViewModel.kt`
- Domain:
  - `domain/usescases/game/.../ReadOfflineRankingUseCase.kt`
  - `domain/usescases/game/.../RecordOfflineMatchResultUseCase.kt`
- Data persistence:
  - `data/couchbase/.../OfflineRankingEntryDoc.kt`
  - `data/couchbase/.../OfflineRankingEntryCBDataSource.kt`
  - `data/repositories/.../IOfflineRankingRepository.kt`
  - `data/repositories/.../OfflineRankingRepository.kt`

Replication notes:

1. Keep online ranking explicitly as placeholder UI until backend flow exists.
2. Persist offline ranking by player with independent counters for civilian and impostor wins.
3. Update ranking counters only when a round reaches final result.
4. Render offline ranking as a table sorted by total wins.

