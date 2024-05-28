plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("io.realm.kotlin")
    id("kotlin-parcelize")
}

val javaVersion = JavaVersion.toVersion(libs.versions.jvmTarget.get())

android {
    namespace = "com.jobik.shkiper"
    compileSdk = libs.versions.androidCompileSdk.get().toIntOrNull()

    defaultConfig {
        applicationId = "com.jobik.shkiper"
        minSdk = libs.versions.androidMinSdk.get().toIntOrNull()
        targetSdk = libs.versions.androidTargetSdk.get().toIntOrNull()
        versionCode = libs.versions.versionCode.get().toIntOrNull()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            this.enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

//    splits {
//        // Application Binary Interface
//        abi {
//            isEnable = true
//            reset()
//            include("armeabi-v7a", "arm64-v8a", "x86_64")
//            isUniversalApk = true
//        }
//    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

val composeVersion by extra("1.5.4")
val accompanistVersion by extra("0.32.0")

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:${project.extra["composeVersion"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${project.extra["composeVersion"]}")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${project.extra["composeVersion"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${project.extra["composeVersion"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${project.extra["composeVersion"]}")

    // Splash screen API (not work when android less than 12)
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation Compose
    // implementation("androidx.navigation:navigation-compose:$accompanist_version")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.4")

    // Pager and Indicators - Accompanist
    // implementation("com.google.accompanist:accompanist-pager:$accompanist_version")
    implementation("com.google.accompanist:accompanist-pager-indicators:${project.extra["accompanistVersion"]}")

    // DataStore Preferences
    // implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Dagger - Hilt
//    kapt("com.google.dagger:hilt-android-compiler:${project.extra["hiltVersion"]}")
//    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(libs.hilt)
    kapt(libs.dagger.hilt.compiler)

    // Icons
    implementation("androidx.compose.material:material-icons-extended:${project.extra["composeVersion"]}")

    // UI controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:${project.extra["accompanistVersion"]}")

    // Livedata
    // implementation("androidx.compose.runtime:runtime-livedata:$compose_version")

    // Mongo Realm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.realm.kotlin:library-base:1.16.0")
    // implementation("io.realm:realm-android-library:10.9.0")

    // Wheel DateTime picker
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // Customize Calendar
    implementation("com.kizitonwose.calendar:compose:2.5.0")

    // JSON serialization
    implementation("com.google.code.gson:gson:2.10")

    // Coil for images
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Jsoup for link preview
    implementation("org.jsoup:jsoup:1.14.3")

    // TedPermission permission provider
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.3.0")

    // In-app updates
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // In-app purchases
    implementation("com.android.billingclient:billing-ktx:6.0.1")

    // Glance Widgets
    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.0.0")
    // For interop APIs with Material 3
    implementation("androidx.glance:glance-material3:1.0.0")

    // Compose Rich Editor
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05-k2")

    // Compose image capture
    implementation("dev.shreyaspatil:capturable:1.0.3")

    // Compose collapsing toolbar (Official latest update in 2022)
    // implementation("me.onebone:toolbar-compose:2.3.5")
    // Compose collapsing toolbar (nonofficial)
    implementation("com.github.GIGAMOLE:ComposeCollapsingToolbar:1.0.8")
}
