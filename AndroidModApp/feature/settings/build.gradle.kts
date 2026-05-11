plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.settings"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)
    api(projects.core.resources)

    api(projects.domain.usescases.settings)
    api(projects.domain.usescases.authentication)
    implementation(libs.material.icons.extended)
}

