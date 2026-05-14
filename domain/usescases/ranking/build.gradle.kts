plugins {
    alias(libs.plugins.buildlogic.domain)
}

android {
    namespace = "es.sebas1705.domain.usescases.ranking"
}

dependencies {
    api(projects.core.common)
    api(projects.data.repositories)
    api(projects.domain.models)
}

