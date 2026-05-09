plugins {
    alias(libs.plugins.buildlogic.core)
}

android {
    namespace = "es.sebas1705.core.designsystem"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)

    api(projects.domain.models)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
}