plugins {
    alias(libs.plugins.shkiper.library)
    alias(libs.plugins.shkiper.compose)
    alias(libs.plugins.shkiper.hilt)
}

android.namespace = "com.jobik.android_widgets"

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Glance Widgets
    // For AppWidgets support
    implementation (libs.androidx.glance.appwidget)

    // For interop APIs with Material 3
    implementation (libs.androidx.glance.material3)
}

