plugins {
    id("buildlogic.android.feature")
}

android {
    namespace = "es.sebas1705.feature.home.face.mode"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)
    api(projects.core.resources)
    api(projects.domain.models)
    api(projects.domain.usescases.game)
    implementation(libs.lottie.compose)
}

