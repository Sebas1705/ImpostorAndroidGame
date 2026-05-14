package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that configures the dependencies for the Datastore library.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class CouchbaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            dependencies {
                implementation(project.libs.findLibrary("couchbase").get())
            }
        }
    }
}