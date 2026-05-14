package libraries

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin that configures Detekt for static analysis.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val isCi = providers.environmentVariable("CI")
                .orElse("false")
                .map { it.equals("true", ignoreCase = true) }
                .get()

            val moduleBaselineFile = rootProject.file(
                "config/detekt/baselines/${path.trim(':').replace(':', '-')}.xml"
            )

            extensions.configure(DetektExtension::class.java) {
                // Point to a default config file if it exists, otherwise use default rules
                config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                allRules = false
                parallel = true
                ignoreFailures = !isCi
                basePath = rootDir.absolutePath

                if (moduleBaselineFile.exists()) {
                    baseline = moduleBaselineFile
                }
            }

            tasks.withType(Detekt::class.java).configureEach {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    md.required.set(false)
                }
            }
        }
    }
}