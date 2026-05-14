plugins {
    alias(libs.plugins.buildlogic.data)
}

android {
    namespace = "es.sebas1705.data.room"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    api(projects.core.common)
    api(projects.data.analytics)
    implementation(libs.retrofit.gson)
    api(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
