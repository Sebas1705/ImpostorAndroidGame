package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that configures the Compose dependencies in the project.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply{
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            dependencies {
                // Compose dependencies with libs implementation
                implementation(libs.findLibrary("ui").get())
                implementation(libs.findLibrary("ui-graphics").get())
                implementation(libs.findLibrary("ui-tooling").get())
                implementation(libs.findLibrary("ui-tooling-preview").get())
                implementation(libs.findLibrary("ui-text-google-fonts").get())
                implementation(libs.findLibrary("kotlinx-collections-immutable").get())
            }
        }
    }
}