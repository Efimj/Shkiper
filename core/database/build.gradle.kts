plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.efim.shkiper.core.database"

dependencies {
    api(projects.core.resources)

    // Mongo Realm
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.library.base)

    implementation(libs.androidx.glance.appwidget)
    implementation(projects.feature.androidWidgets)

    implementation(projects.core.domain)
}