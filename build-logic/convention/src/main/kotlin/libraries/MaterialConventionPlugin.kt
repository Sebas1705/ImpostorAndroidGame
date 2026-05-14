package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that configures the Material dependencies in the project.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class MaterialConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.findLibrary("material").get())
                implementation(libs.findLibrary("material3").get())
                implementation(libs.findLibrary("material3-windowsizeclass").get())
                implementation(libs.findLibrary("material-icons-extended").get())
                implementation(libs.findLibrary("material3-adaptive").get())
                implementation(libs.findLibrary("material3-adaptive-layout").get())
                implementation(libs.findLibrary("material3-adaptive-navigation").get())
                implementation(libs.findLibrary("material3-adaptive-navigation-suite-android").get())
            }
        }
    }
}