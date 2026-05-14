plugins {
    alias(libs.plugins.buildlogic.data)
    alias(libs.plugins.buildlogic.firebase)
    alias(libs.plugins.buildlogic.unit.test)
}

android {
    namespace = "es.sebas1705.data.analytics"
}

dependencies {
    api(projects.core.common)
}
