plugins {
    alias(libs.plugins.buildlogic.domain)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "es.sebas1705.domain.usescases.trivia"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.data.repositories)
    api(projects.domain.models)
    api(projects.domain.mappers)
}