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

    api(projects.domain.usescases.settings)
    api(projects.domain.usescases.analytics)
    api(projects.domain.usescases.opendb)

    api(projects.feature.splash)
    api(projects.feature.mvisample)
    api(projects.feature.mvvmsample)
}
