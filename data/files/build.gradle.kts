plugins {
    alias(libs.plugins.buildlogic.data)
}

android {
    namespace = "es.sebas1705.data.files"
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)

    implementation(libs.kotlin.serialization.json)
}