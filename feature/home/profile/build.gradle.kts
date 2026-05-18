plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.home.profile"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)
    api(projects.domain.usescases.authentication)
    api(projects.domain.usescases.ranking)
    api(projects.domain.usescases.game)
    implementation(libs.coil.compose)
}

