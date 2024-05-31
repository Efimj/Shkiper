plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.efim.shkiper.core.helper"

dependencies {
    api(projects.core.resources)

    // Jsoup for link preview
    implementation(libs.jsoup)
}