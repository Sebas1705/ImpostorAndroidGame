plugins {
    alias(libs.plugins.buildlogic.data)
    alias(libs.plugins.buildlogic.firebase)
    alias(libs.plugins.buildlogic.credential)
    alias(libs.plugins.buildlogic.unit.test)
}

android {
    namespace = "es.sebas1705.data.authentication"
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)

    // Robolectric runs on top of JUnit4.
    testImplementation(libs.junit.junit)
}
