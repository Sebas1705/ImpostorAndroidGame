import com.android.build.api.dsl.ApplicationExtension
import es.sebas1705.convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * A plugin that configures the Android flavors of the application.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class FlavorsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                configureFlavors(this)
            }
        }
    }
}