plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.efim.shkiper.core.domain"

dependencies {
    api(projects.core.resources)

    // Mongo Realm
    implementation(libs.library.base)
}