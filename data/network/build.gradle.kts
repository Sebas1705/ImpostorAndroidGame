plugins {
    alias(libs.plugins.buildlogic.data)
}

android {
    namespace = "es.sebas1705.data.network"
}

dependencies {
    api(projects.core.common)
    api(projects.domain.models)
    implementation(libs.kotlin.serialization.json)
}
