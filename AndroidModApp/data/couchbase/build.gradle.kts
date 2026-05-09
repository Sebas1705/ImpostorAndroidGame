plugins {
    alias(libs.plugins.buildlogic.data)
    alias(libs.plugins.buildlogic.couchbase)
    alias(libs.plugins.buildlogic.unit.test)
}

android {
    namespace = "es.sebas1705.data.couchbase"
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)
}