plugins {
    alias(libs.plugins.buildlogic.core)
    alias(libs.plugins.buildlogic.credential)
    alias(libs.plugins.buildlogic.unit.test)
}

android {
    namespace = "es.sebas1705.common"
}

dependencies {
    api(projects.core.resources)
    api(libs.material3.windowsizeclass)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
}
