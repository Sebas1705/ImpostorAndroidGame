import com.android.build.gradle.LibraryExtension
import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Plugin that applies common configuration for Android feature modules.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("buildlogic.android.library")
                apply("buildlogic.android.hilt")
                apply("buildlogic.android.jacoco")
                apply("buildlogic.android.compose")
                apply("buildlogic.android.firebase")
                apply("buildlogic.android.lifecycle")
                apply("buildlogic.android.navigation")
                apply("buildlogic.android.material")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures.buildConfig = true
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            dependencies {
                implementation(libs.findLibrary("kotlin-serialization-json").get())
            }
        }
    }
}