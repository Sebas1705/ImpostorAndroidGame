plugins {
    alias(libs.plugins.buildlogic.domain)
}

android {
    namespace = "es.sebas1705.domain.models"
}

dependencies {
    api(projects.core.common)

    implementation(libs.firebase.auth)
}
