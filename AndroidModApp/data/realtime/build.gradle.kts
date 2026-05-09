plugins {
    alias(libs.plugins.buildlogic.data)
    alias(libs.plugins.buildlogic.firebase)
}

android {
    namespace = "es.sebas1705.data.realtime"
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)
}