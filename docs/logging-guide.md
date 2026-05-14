# Logging Guide

Project logging is centralized in `core/common/src/main/java/es/sebas1705/common/utlis/extensions/types/Any.kt`.

## What You Get By Default (dev variants only)

Every call to `logD`, `logI`, `logW`, `logE`, `logV`, and `logWTF` is enriched with:

- level marker (example: `[D>]`, `[E!]`)
- domain marker inferred from caller package (example: `NAV`, `MVI`, `DB`, `GAME`, `AUTH`, `NET`)
- thread name
- file and line number
- automatic chunk splitting for long messages

Example output shape:

`[D>][NAV][main][HomeDesign.kt:210] start offline game users=6 selectedCategories=4 mode=Classic`

## Domain Inference Rules

Current domain classification uses caller package/name patterns:

- `NAV`: `.main.` or `.nav.`
- `GAME`: `.offlinegame.`
- `DB`: `.couchbase.`, `.room.`, `.repositories.`
- `AUTH`: `.authentication.`, `.login.`
- `MVI`: `.mvi.` or class name contains `ViewModel`
- `NET`: `.network.`, `.retrofit.`
- `GEN`: fallback when no rule matches

## Logcat Filter Presets (recommended)

Create and save these filters in Android Studio Logcat:

1. `App Dev Logs`
   - Package: `es.sebas1705.impostorandroidgame.dev`
   - Message contains: `[`

2. `Navigation`
   - Message regex: `\[.*\]\[NAV\].*`

3. `Game Flow`
   - Message regex: `\[.*\]\[(GAME|MVI)\].*`

4. `Data Layer`
   - Message regex: `\[.*\]\[(DB|NET)\].*`

5. `Errors`
   - Level: `Error`
   - Message regex: `\[E!\].*`

## Usage Rules

- Use the existing extensions (`Any.logD`, `Any.logI`, etc.) instead of direct `android.util.Log`.
- Keep log payloads concise and contextual (`action + IDs + counts + outcome`).
- For sensitive values, mask or summarize before logging.

