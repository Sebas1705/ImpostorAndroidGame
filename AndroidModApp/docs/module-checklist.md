# Module Checklist

Use this checklist when adding a new module.

## Required

- [ ] Module is included in `settings.gradle.kts`
- [ ] Correct convention plugin is applied
- [ ] No hardcoded dependency versions
- [ ] Unit test dependencies are present when module has logic
- [ ] Package name and namespace follow repository naming conventions
- [ ] Module builds with `assembleDebug` (or relevant variant)
- [ ] Module passes Detekt
- [ ] Module docs references are updated when public behavior or boundaries change

## Recommended

- [ ] Add at least one unit test for core behavior
- [ ] Avoid wildcard imports and locale-sensitive formatting pitfalls
- [ ] Add KDoc for public types exposed outside module boundaries
- [ ] Keep module public API small and intentional
- [ ] If debug tooling is added, keep it variant-scoped (`debugImplementation`)

## Verification commands

```powershell
.\gradlew.bat :your:module:assembleDebug
.\gradlew.bat :your:module:detekt
.\gradlew.bat :your:module:testDebugUnitTest
.\gradlew.bat dependencyUpdates --no-parallel
```

