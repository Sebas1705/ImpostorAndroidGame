plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.offlinegame"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)
    api(projects.core.resources)

    api(projects.domain.models)
    api(projects.domain.usescases.game)
    api(projects.domain.usescases.ranking)

    implementation(libs.material.icons.extended)
}

