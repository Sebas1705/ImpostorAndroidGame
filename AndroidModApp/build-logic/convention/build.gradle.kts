plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

// Ensure published variants target JVM 21 for compatibility with root build JDK
tasks.withType<Jar>().configureEach {
    manifest {
        attributes("Target-JVM" to "21")
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.org.jacoco.core)
}

gradlePlugin {
    plugins {
        ///////////////MODULES////////////////
        //Register the plugin that applies common configuration for Android applications
        register("androidApplication") {
            id = "buildlogic.android.application"
            implementationClass = "AppConventionPlugin"
        }
        //Register the plugin that applies common configuration for Android feature modules
        register("androidFeature") {
            id = "buildlogic.android.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        //Register the plugin that applies common configuration for Android core modules
        register("androidCore") {
            id = "buildlogic.android.core"
            implementationClass = "CoreConventionPlugin"
        }
        //Register the plugin that applies common configuration for Android data modules
        register("androidData") {
            id = "buildlogic.android.data"
            implementationClass = "DataConventionPlugin"
        }
        //Register the plugin that applies common configuration for Android domain modules
        register("androidDomain") {
            id = "buildlogic.android.domain"
            implementationClass = "DomainConventionPlugin"
        }

        //////////////LIBRARIES////////////////
        register("androidLibrary") {
            id = "buildlogic.android.library"
            implementationClass = "libraries.LibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "buildlogic.android.hilt"
            implementationClass = "libraries.HiltConventionPlugin"
        }
        register("androidFirebase") {
            id = "buildlogic.android.firebase"
            implementationClass = "libraries.FirebaseConventionPlugin"
        }
        register("androidRetrofit") {
            id = "buildlogic.android.retrofit"
            implementationClass = "libraries.RetrofitConventionPlugin"
        }
        register("androidCompose") {
            id = "buildlogic.android.compose"
            implementationClass = "libraries.ComposeConventionPlugin"
        }
        register("androidCredential") {
            id = "buildlogic.android.credential"
            implementationClass = "libraries.CredentialConventionPlugin"
        }
        register("androidDatastore") {
            id = "buildlogic.android.datastore"
            implementationClass = "libraries.DatastoreConventionPlugin"
        }
        register("androidCouchbase") {
            id = "buildlogic.android.couchbase"
            implementationClass = "libraries.CouchbaseConventionPlugin"
        }
        register("androidLifecycle") {
            id = "buildlogic.android.lifecycle"
            implementationClass = "libraries.LifecycleConventionPlugin"
        }
        register("androidMaterial") {
            id = "buildlogic.android.material"
            implementationClass = "libraries.MaterialConventionPlugin"
        }
        register("androidNavigation") {
            id = "buildlogic.android.navigation"
            implementationClass = "libraries.NavigationConventionPlugin"
        }
        register("androidUnitTest") {
            id = "buildlogic.android.unit.test"
            implementationClass = "libraries.UnitTestConventionPlugin"
        }
        register("androidJacoco") {
            id = "buildlogic.android.jacoco"
            implementationClass = "libraries.JacocoConventionPlugin"
        }
        register("androidDetekt") {
            id = "buildlogic.android.detekt"
            implementationClass = "libraries.DetektConventionPlugin"
        }

        //////////////FLAVORS////////////////
        register("androidFlavors") {
            id = "buildlogic.android.flavors"
            implementationClass = "FlavorsConventionPlugin"
        }
    }
}