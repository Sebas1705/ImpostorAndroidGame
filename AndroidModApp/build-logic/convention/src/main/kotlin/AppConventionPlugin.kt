import com.android.build.api.dsl.ApplicationExtension
import es.sebas1705.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * A plugin that configures an Android application project.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class AppConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val isUnitTestRun = gradle.startParameter.taskNames.any { it.contains("test", ignoreCase = true) }
            val hasGoogleServicesConfig =
                rootProject.file("./app/google-services.json").exists() ||
                    rootProject.file("./app/src/debug/google-services.json").exists() ||
                    rootProject.file("./app/src/Dev/google-services.json").exists() ||
                    rootProject.file("./app/src/DevDebug/google-services.json").exists()

            with(pluginManager) {
                apply("com.android.application")
                if (!isUnitTestRun && hasGoogleServicesConfig) {
                    apply("com.google.firebase.crashlytics")
                    apply("com.google.gms.google-services")
                } else {
                    logger.lifecycle("Skipping Firebase Gradle plugins: test run or missing google-services.json.")
                }
                apply("org.jetbrains.kotlin.kapt")
                apply("org.jetbrains.kotlin.android")
                apply("buildlogic.android.hilt")
                apply("buildlogic.android.compose")
                apply("buildlogic.android.lifecycle")
                apply("buildlogic.android.flavors")
                apply("buildlogic.android.jacoco")
                apply("buildlogic.android.detekt")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                defaultConfig.minSdk = 31
            }
        }
    }
}