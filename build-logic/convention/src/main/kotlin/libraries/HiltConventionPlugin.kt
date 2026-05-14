package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that configures the dependencies for the Hilt library.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies{
                add("ksp", libs.findLibrary("hilt-compiler").get())
            }

            //This ensures that the plugin is applied after the android base plugin
            pluginManager.withPlugin("com.android.base") {
                //plugin necessary to integrate Hilt in the Android project.
                pluginManager.apply("dagger.hilt.android.plugin")
                dependencies {
                    //Dependency that provides the core functionality of Hilt for DI.
                    implementation(libs.findLibrary("hilt-android").get())
                    implementation(libs.findLibrary("hilt-navigation").get())
                }
            }
        }
    }
}