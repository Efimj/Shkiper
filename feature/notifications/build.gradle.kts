plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.efim.shkiper.feature.notifications"

dependencies {
    api(projects.core.resources)

    implementation(projects.core.database)
    implementation(projects.core.ui)
}