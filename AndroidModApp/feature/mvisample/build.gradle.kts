plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.mvisample"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)

    api(projects.domain.usescases.analytics)
    api(projects.domain.usescases.settings)
}