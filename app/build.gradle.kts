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
        isCoreLibraryDesugaringEnabled = true
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

dependencies {
    coreLibraryDesugaring(libs.desugaring)

//    implementation(projects.feature.androidWidgets)

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
    implementation(libs.androidx.foundation)

    implementation(libs.material)

    // Splash screen API (not work when android less than 12)
    implementation(libs.androidx.core.splashscreen)

    // Navigation Compose
    // implementation("androidx.navigation:navigation-compose:$accompanist_version")
    implementation(libs.androidx.navigation.runtime.ktx)

    // Pager and Indicators - Accompanist
    // implementation("com.google.accompanist:accompanist-pager:$accompanist_version")
    implementation(libs.accompanist.pager.indicators)

    // DataStore Preferences
    // implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Dagger - Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt)
    kapt(libs.dagger.hilt.compiler)

    // Icons
    implementation(libs.androidx.material.icons.extended)

    // UI controller
    implementation(libs.accompanist.systemuicontroller)

    // Livedata
    // implementation("androidx.compose.runtime:runtime-livedata:$compose_version")

    // Mongo Realm
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.library.base)
    // implementation("io.realm:realm-android-library:10.9.0")

    // Wheel DateTime picker
    implementation(libs.wheelpickercompose)

    // Customize Calendar
    implementation(libs.compose)

    // JSON serialization
    implementation(libs.gson)

    // Coil for images
    implementation(libs.coil.compose)

    // Jsoup for link preview
    implementation(libs.jsoup)

    // TedPermission permission provider
    implementation(libs.tedpermission.coroutine)

    // In-app updates
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // In-app purchases
    implementation(libs.billing.ktx)

    // Glance Widgets
    // For AppWidgets support
    implementation(libs.androidx.glance.appwidget)

    // For interop APIs with Material 3
    implementation(libs.androidx.glance.material3)

    // Compose Rich Editor
    implementation(libs.richeditor.compose)

    // Compose image capture
    implementation(libs.capturable)

    // Compose collapsing toolbar (Official latest update in 2022)
    // implementation("me.onebone:toolbar-compose:2.3.5")
    // Compose collapsing toolbar (nonofficial)
    implementation(libs.composecollapsingtoolbar)
}
