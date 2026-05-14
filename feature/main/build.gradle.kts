plugins {
    alias(libs.plugins.buildlogic.feature)
}

android {
    namespace = "es.sebas1705.feature.main"
}

dependencies {
    api(projects.core.common)
    api(projects.core.ui)
    api(projects.core.designsystem)

    api(projects.domain.usescases.authentication)
    api(projects.domain.models)
    api(projects.domain.usescases.settings)

    api(projects.feature.debug)
    api(projects.feature.home.face.nav)
    api(projects.feature.login)
    api(projects.feature.offlinegame)
    api(projects.feature.settings)
    api(projects.feature.splash)

    implementation(libs.material.icons.extended)
}
