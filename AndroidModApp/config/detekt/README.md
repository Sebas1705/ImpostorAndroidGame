# Detekt Configuration Guide

Shared Detekt configuration is in `config/detekt/detekt.yml`.

## Quality gate rollout

This template uses gradual hardening:

1. Start with a high `build.maxIssues`
2. Fix findings continuously
3. Lower threshold in small increments
4. Reach strict mode (`maxIssues: 0`) when practical

## Optional baselines

You can keep legacy findings in per-module baseline files:

- Folder: `config/detekt/baselines/`
- Naming: `<module-path-with-dashes>.xml`
- Example: `data-room.xml`, `feature-main.xml`

The convention plugin auto-loads a baseline when the file exists.

## Useful commands

```powershell
.\gradlew.bat detekt
.\gradlew.bat :feature:main:detekt
```

