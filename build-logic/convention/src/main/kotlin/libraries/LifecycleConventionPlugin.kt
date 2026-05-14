package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that configures the dependencies for the Lifecycle library.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class LifecycleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            dependencies {
                implementation(project.libs.findLibrary("lifecycle-runtime-ktx").get())
                implementation(project.libs.findLibrary("lifecycle-viewmodel-compose").get())
                implementation(project.libs.findLibrary("lifecycle-runtime-compose").get())
                implementation(project.libs.findLibrary("lifecycle-viewmodel-nav3").get())
                implementation(project.libs.findLibrary("livedata-runtime").get())
            }
        }
    }
}