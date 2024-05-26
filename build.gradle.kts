buildscript {
    val composeVersion by extra("1.5.4")
    val accompanistVersion by extra("0.32.0")
    val hiltVersion by extra("2.46")

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath("io.realm:realm-gradle-plugin:10.4.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("io.realm.kotlin") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("com.android.test") version "8.1.0" apply false
}