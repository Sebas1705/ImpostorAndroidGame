plugins {
    alias(libs.plugins.buildlogic.data)
    alias(libs.plugins.buildlogic.retrofit)
}

android {
    namespace = "es.sebas1705.data.retrofit"
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)
}