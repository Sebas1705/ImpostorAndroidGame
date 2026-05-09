package libraries

import es.sebas1705.convention.implementation
import es.sebas1705.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin that configures the dependencies for the Credential library.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class CredentialConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.dependencies.apply {
            implementation(project.libs.findLibrary("play-services-auth").get())
            implementation(project.libs.findLibrary("googleid").get())
            implementation(project.libs.findLibrary("credentials").get())
            implementation(project.libs.findLibrary("credentials-play-services-auth").get())
        }
    }
}