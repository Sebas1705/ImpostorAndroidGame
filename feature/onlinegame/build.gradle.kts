plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.onlinegame"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)
    api(projects.core.resources)

    api(projects.domain.models)
    api(projects.domain.managers)
    api(projects.domain.usescases.game)
    api(projects.domain.usescases.ranking)

    api(projects.data.network)
    api(projects.data.repositories)

    implementation(libs.material.icons.extended)
    implementation(libs.kotlin.serialization.json)
}
