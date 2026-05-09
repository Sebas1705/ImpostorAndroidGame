plugins {
    alias(libs.plugins.buildlogic.domain)
    alias(libs.plugins.buildlogic.firebase)
}

android {
    namespace = "es.sebas1705.domain.services"
}

dependencies {
    api(projects.core.common)
}