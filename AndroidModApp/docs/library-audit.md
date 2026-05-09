# Library Audit and Practical Recommendations

This audit is based on `gradle/libs.versions.toml` and current module usage.

## Current strengths

- Solid baseline for Android app architecture: Hilt, Retrofit/OkHttp, Room, Datastore, Firebase, Couchbase Lite.
- Good quality stack already present: Detekt, JaCoCo, unit/instrumented test libraries.
- Version catalog is centralized and build logic is modularized.

## High-value additions implemented

1. Leak detection in debug builds
   - Library: `com.squareup.leakcanary:leakcanary-android`
   - Status: implemented in `app` as `debugImplementation`.
   - Why: catches lifecycle and context leaks early.
2. Network inspector in debug builds
   - Library: `com.github.chuckerteam.chucker:library`
   - Status: implemented in `buildlogic.android.retrofit` with `library-no-op` for release.
   - Why: inspect REST traffic directly on device.
3. Dependency update task in CI
   - Plugin already declared (`benmanes-versions`).
   - Status: enabled at root, runnable with `./gradlew dependencyUpdates --no-parallel`.
   - Workflow template available at `config/templates/dependency-report-monthly.workflow.yml.template`.
   - Why: monthly report of outdated dependencies.

## Couchbase runtime inspection options

For Couchbase Lite on Android there is no equivalent to "open SQLite in Android Studio" because storage is not plain SQLite tables you can browse safely.

Practical options:

1. In-app debug inspector (implemented in `feature:mvvmsample`)
   - Refresh all docs and show them on screen.
   - Insert demo docs to verify writes quickly.
2. Sync to backend and inspect from server tooling
   - Replicate to Sync Gateway / Capella and inspect with backend consoles.
3. Export diagnostic snapshot
   - Add a debug-only action to dump docs as JSON for QA sessions.

## What was implemented now

- Runtime Couchbase inspection in the MVVM sample screen.
- Debug tools route (`feature:main`) to refresh diagnostics for Couchbase + OpenDB.
- Chucker-backed OkHttp wiring via build variants in `data:retrofit`.
- LeakCanary in app debug builds.
- Root `dependencyUpdates` task activation for monthly dependency reporting.
- `IMyDocRepository` binding in Hilt module so it can be injected from use cases.
- Use cases in `domain:usescases:settings` to read and insert demo Couchbase docs.

## Monthly review policy

- Run `./gradlew dependencyUpdates --no-parallel` and review the generated report artifact.
- Prioritize security and stable updates first.
- Track alpha/beta upgrades separately unless needed for platform compatibility.



