plugins {
    alias(libs.plugins.buildlogic.domain)
    alias(libs.plugins.buildlogic.unit.test)
}

android {
    namespace = "es.sebas1705.domain.managers"
}

dependencies {
    api(projects.core.resources)
}