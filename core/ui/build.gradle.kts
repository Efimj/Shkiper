plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.efim.shkiper.core.ui"

dependencies {
    api(projects.core.resources)

    // Wheel DateTime picker
    implementation(libs.wheelpickercompose)

    // Customize Calendar
    implementation(libs.compose)
}