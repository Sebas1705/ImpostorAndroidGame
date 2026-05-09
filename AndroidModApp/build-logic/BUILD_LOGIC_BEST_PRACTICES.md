# Build-Logic Convention Plugins Best Practices

**Last Updated**: January 13, 2026  
**Version**: 1.0  
**Audience**: Developers who maintain and extend Gradle convention plugins

---

## 📋 Table of Contents

1. [Overview](#overview)
2. [Module Structure](#module-structure)
3. [Plugin Anatomy](#plugin-anatomy)
4. [Common Patterns](#common-patterns)
5. [Design Decisions](#design-decisions)
6. [Reusability in Other Projects](#reusability-in-other-projects)
7. [Quality Gates and CI Rollout](#quality-gates-and-ci-rollout)

---

## Overview

The convention plugins system in `build-logic/` is designed to:

- ✅ **Centralize configuration** common to Gradle
- ✅ **Reduce duplication** in module `build.gradle.kts` files
- ✅ **Improve maintainability** by allowing global changes
- ✅ **Facilitate onboarding** of new developers with sensible defaults
- ✅ **Be reusable** across multiple Android projects

### Design Philosophy

```
Convention Over Configuration (CoC):
├── Sensible defaults for 90% of modules
├── Allows explicit override when necessary
├── Document exceptions clearly
└── Keep simplicity: if it requires >50 lines, it's too complex
```

---

## Module Structure

### Plugin Hierarchy

```
build-logic/
├── convention/
│   └── src/main/kotlin/
│       ├── AppConventionPlugin.kt          ← Main application
│       ├── CoreConventionPlugin.kt         ← core/ modules
│       ├── DataConventionPlugin.kt         ← data/ modules
│       ├── DomainConventionPlugin.kt       ← domain/ modules
│       ├── FeatureConventionPlugin.kt      ← feature/ modules
│       ├── FlavorsConventionPlugin.kt      ← Build flavors (dev/prod)
│       ├── libraries/                      ← Library-specific plugins
│       │   ├── LibraryConventionPlugin.kt  ← Base for all libraries
│       │   ├── ComposeConventionPlugin.kt  ← Compose UI
│       │   ├── HiltConventionPlugin.kt     ← Dependency Injection
│       │   ├── FirebaseConventionPlugin.kt ← Firebase services
│       │   ├── RetrofitConventionPlugin.kt ← HTTP client
│       │   ├── NavigationConventionPlugin.kt ← Navigation routing
│       │   ├── LifecycleConventionPlugin.kt ← Android Lifecycle
│       │   ├── MaterialConventionPlugin.kt ← Material Design
│       │   ├── CredentialConventionPlugin.kt ← Auth credentials
│       │   ├── DatastoreConventionPlugin.kt ← Preferences
│       │   ├── CouchbaseConventionPlugin.kt ← Couchbase DB
│       │   ├── UnitTestConventionPlugin.kt ← Unit testing setup
│       │   └── JacocoConventionPlugin.kt   ← Code coverage
│       └── es/sebas1705/convention/        ← Shared utilities
│           ├── KotlinAndroid.kt            ← Kotlin/Java configuration
│           ├── ProjectExtensions.kt        ← Gradle API extensions
│           └── CoreFlavor.kt               ← Flavor configuration
└── settings.gradle.kts
```

### Plugin Registration

```kotlin
// build-logic/convention/build.gradle.kts
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "buildlogic.android.application"
            implementationClass = "AppConventionPlugin"
        }
        // ... more plugins
    }
}
```

**Naming Convention**:
- **Plugin ID**: `buildlogic.android.<type>.<purpose>`
  - Examples: `buildlogic.android.application`, `buildlogic.android.compose`
- **Class name**: `<NameWithoutSpaces>ConventionPlugin`
  - Example: `AppConventionPlugin`, `ComposeConventionPlugin`

---

## Plugin Anatomy

### Basic Structure

```kotlin
package libraries  // or root of same file

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.configure

/**
 * Configures [LibraryX] dependency in Android projects.
 *
 * @since 1.0.0
 * @author Name 2025-01-13
 */
class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 1. Apply necessary plugins
            pluginManager.apply {
                apply("com.example.plugin")
            }
            
            // 2. Configure extensions (if necessary)
            extensions.configure<SomeExtension> {
                // configuration here
            }
            
            // 3. Add dependencies
            dependencies {
                implementation(libs.findLibrary("library").get())
                implementation(libs.findLibrary("library.component").get())
            }
        }
    }
}
```

### Main Parts

#### 1️⃣ **Plugin Application**

```kotlin
// Minimum: dependencies only
pluginManager.apply("com.android.library")

// Intermediate: apply multiple plugins
pluginManager.apply {
    apply("com.android.library")
    apply("kotlin-android")
    apply("org.jetbrains.kotlin.plugin.compose")
}
```

#### 2️⃣ **Extension Configuration**

```kotlin
// To modify applied plugin configuration
extensions.configure<LibraryExtension> {
    compileSdk = 36
    defaultConfig.minSdk = 30
}

// For custom extensions
extensions.create<CustomOptions>("customOptions") {
    someProperty = true
}
```

#### 3️⃣ **Dependency Management**

```kotlin
dependencies {
    // Implementation
    implementation(libs.findLibrary("library").get())
    
    // Compile-only (not included in APK/AAR)
    compileOnly(libs.findLibrary("annotations").get())
    
    // Test dependencies
    testImplementation(libs.findLibrary("junit").get())
    
    // API (export to dependents)
    api(libs.findLibrary("library").get())
}
```

### Required Comments

```kotlin
/**
 * Brief description of what this plugin configures.
 * 
 * Applied to modules that need [Library] support.
 * Automatically applies [plugin.Id] and adds [dependency] dependencies.
 *
 * @since [version]
 * @author [name] [date]
 * @see [RelatedPlugin] for related configuration
 */
class LibraryConventionPlugin : Plugin<Project> {
    // ...
}
```

---

## Common Patterns

### Pattern 1: Plugin That Only Adds Dependencies

```kotlin
/**
 * Adds Material Design 3 dependencies.
 */
class MaterialConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.findLibrary("material3").get())
                implementation(libs.findLibrary("material-icons-extended").get())
                // ... more dependencies
            }
        }
    }
}
```

**When to use**: When you only need to inject dependencies without additional configuration.

---

### Pattern 2: Plugin That Configures + Dependencies

```kotlin
/**
 * Configures Hilt dependency injection framework.
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 1. Apply plugins
            pluginManager.apply {
                apply("com.google.dagger.hilt.android")
                apply("kotlin-kapt")  // Required for Hilt in modules
            }
            
            // 2. Add dependencies
            dependencies {
                implementation(libs.findLibrary("hilt.android").get())
                kapt(libs.findLibrary("hilt.compiler").get())
                
                // Compose integration if available
                implementation(libs.findLibrary("hilt.navigation").get())
            }
        }
    }
}
```

**When to use**: When you need to apply plugins + add dependencies together.

---

### Pattern 3: Conditional Plugin

```kotlin
/**
 * Configures Firebase only when the app module is building release variant.
 */
class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }
            
            dependencies {
                implementation(libs.findLibrary("firebase.analytics").get())
                implementation(libs.findLibrary("firebase.crashlytics").get())
                
                // Conditional: only in release builds
                implementation(libs.findLibrary("firebase.messaging").get())
            }
        }
    }
}
```

**Note**: Complex conditions are better in `FlavorsConventionPlugin`.

---

### Pattern 4: Testing Plugin

```kotlin
/**
 * Configures unit testing framework and dependencies.
 */
class UnitTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Don't apply plugins here (testing.buildFeatures.testing is default)
            
            dependencies {
                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("mockito").get())
                testImplementation(libs.findLibrary("turbine").get())
                
                androidTestImplementation(libs.findLibrary("espresso.core").get())
            }
        }
    }
}
```

---

### Pattern 5: Plugin That Configures and Registers Tasks

```kotlin
/**
 * Configures Jacoco code coverage for Android modules.
 */
class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 1. Apply plugin
            pluginManager.apply("jacoco")
            
            // 2. Configure extension
            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }
            
            // 3. Register custom tasks
            tasks.register("generateCoverageReport") {
                group = "verification"
                description = "Generates code coverage report"
                // Task configuration here
            }
        }
    }
}
```

---

## Design Decisions

### ❓ When to Create a New Plugin?

**CREATE a new plugin if:**
- ✅ Configuration is reused across 2+ modules
- ✅ Configuration is sufficiently complex (>20 lines)
- ✅ It's a clear library/framework (Firebase, Hilt, Retrofit)
- ✅ It will be documented and maintained

**DO NOT CREATE if:**
- ❌ It's only 1-2 dependencies without configuration
- ❌ It's specific to ONE module only
- ❌ It's temporary/experimental configuration

---

### ❓ When to Use `implementation()` vs `api()`?

```kotlin
dependencies {
    // API: dependents can see this dependency
    api(libs.findLibrary("hilt.android").get())
    
    // Implementation: hidden from dependents (preferred)
    implementation(libs.findLibrary("hilt.compiler").get())
}
```

**General Rule**:
- Use `api()` if other modules need to use it
- Use `implementation()` by default (hides internal details)

---

### ❓ When to Use `compileOnly`?

```kotlin
dependencies {
    // Not included in final APK/AAR, only in compilation
    compileOnly(libs.findLibrary("javax.inject").get())
}
```

**Use cases**:
- Annotations (@Inject, @Qualifier, etc.)
- Interfaces/contracts that others implement
- Large libraries already in the platform

---

## Reusability in Other Projects

### Structure for Export

To reuse `build-logic` in other projects:

```
Your New Project/
├── build-logic/                    ← Copy from AndroidModApp
│   ├── convention/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── gradle/
│   └── libs.versions.toml          ← Adapt for your project
├── build.gradle.kts
├── settings.gradle.kts
└── ...modules
```

### Adaptation Steps

1. **Copy `build-logic/`** completely
2. **Adapt `gradle/libs.versions.toml`** to your dependencies
3. **Modify plugins** according to your new project's specific libraries
4. **Remove unnecessary plugins** (e.g., if you don't use Couchbase)
5. **Create new plugins** for specific libraries

### Example: Remove Unnecessary Plugin

```kotlin
// BEFORE: In build-logic/convention/build.gradle.kts
register("androidCouchbase") {
    id = "buildlogic.android.couchbase"
    implementationClass = "libraries.CouchbaseConventionPlugin"
}

// AFTER: Comment it out if you don't need Couchbase
// register("androidCouchbase") {
//     id = "buildlogic.android.couchbase"
//     implementationClass = "libraries.CouchbaseConventionPlugin"
// }
```

---

### Example: Create New Plugin

To add support for a new library:

```kotlin
// build-logic/convention/src/main/kotlin/libraries/ExampleLibConventionPlugin.kt
package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures ExampleLib integration for Android modules.
 * 
 * Adds necessary dependencies for ExampleLib framework.
 *
 * @since 1.0.0
 * @author Your Name 2025-01-13
 */
class ExampleLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("example.lib.plugin")  // If available
            }
            
            dependencies {
                implementation(libs.findLibrary("example.lib").get())
                implementation(libs.findLibrary("example.lib.android").get())
            }
        }
    }
}
```

```kotlin
// Register in build-logic/convention/build.gradle.kts
register("androidExampleLib") {
    id = "buildlogic.android.example.lib"
    implementationClass = "libraries.ExampleLibConventionPlugin"
}
```

```toml
# Add to gradle/libs.versions.toml
[versions]
example-lib = "1.2.3"

[libraries]
example-lib = { group = "com.example", name = "example-lib", version.ref = "example-lib" }
example-lib-android = { group = "com.example", name = "example-lib-android", version.ref = "example-lib" }
```

```kotlin
// Use in module
plugins {
    alias(libs.plugins.buildlogic.library)
    alias(libs.plugins.buildlogic.example.lib)  ← New plugin
}
```

---

## Quality Gates and CI Rollout

This template now uses a progressive quality strategy:

- Detekt uses the shared config in `config/detekt/detekt.yml`
- CI runs separate jobs for build/lint, detekt, and tests/coverage in `.github/workflows/validate.yml`
- Module standards are documented in `docs/module-standards.md`
- New module review checklist is documented in `docs/module-checklist.md`

When adding new modules, follow the checklist first and only then tighten the quality gates.

---

## Code Review Checklist

Use this checklist before merging changes to `build-logic/`:

```markdown
- [ ] Code compiles without warnings
- [ ] Complete KDoc documentation (author, since)
- [ ] Plugin name in kebab-case (buildlogic.android.x)
- [ ] Class name in PascalCase + ConventionPlugin suffix
- [ ] No hardcoded values (use libs.versions.toml)
- [ ] No module-specific configuration
- [ ] Tests run without errors
- [ ] Documented in BUILD_LOGIC_BEST_PRACTICES.md
- [ ] Does not introduce breaking changes or updates docs
- [ ] Changes grouped logically
```

---

## FAQ

**Q: Can I override the plugin configuration in my module?**  
A: Yes, the plugin configuration is the default. You can override it in your `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.buildlogic.application)
}

android {
    // Override config here
    compileSdk = 37  // Instead of 36 from the plugin
}
```

**Q: Why are some dependencies in `api()` and others in `implementation()`?**  
A: `api()` exposes the dependency to dependent modules. Use it only when the module needs to use its public classes.

**Q: Can I have plugins that depend on other plugins?**  
A: Yes, apply plugins in any order. Gradle resolves the order automatically.

**Q: What happens if I don't apply a plugin in a module?**  
A: Dependencies are not added. You must apply them explicitly in the module's `build.gradle.kts`.

---

## Resources

- **Template Module Standards**: `docs/module-standards.md`
- **Template Module Checklist**: `docs/module-checklist.md`
- **Official Gradle Documentation**: https://docs.gradle.org
- **Kotlin DSL for Gradle**: https://docs.gradle.org/current/userguide/kotlin_dsl.html
- **Android Gradle Plugin Guide**: https://developer.android.com/build

---

**Last Updated**: January 13, 2026  
**Next Review Recommended**: When a major new plugin is added
