package es.sebas1705.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Method that configures the Kotlin Android plugin in the project.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 30
        }

        compileOptions {
            // Align Java compatibility with Java 17 (LTS)
            // As specified in AGENTS.md for broader compatibility
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    configureKotlin()
}

/**
 * Method that configures the Kotlin plugin in the project.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // Align Kotlin JVM target with Java 17
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}