plugins {
    alias(libs.plugins.buildlogic.domain)
}

android {
    namespace = "es.sebas1705.domain.usescases.game"
}

dependencies {
    api(projects.core.common)
    api(projects.data.repositories)
    api(projects.domain.models)
    api(projects.domain.mappers)
}

