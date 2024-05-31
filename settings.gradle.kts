@file:Suppress("UnstableApiUsage")

include(":core:helper")


include(":core:domain")


include(":feature:notifications")


include(":core:database")


include(":core:resources")


include(":core:ui")


include(":ui")


pluginManagement {
    repositories {
        includeBuild("build-logic")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "NotePadApp"

include(":app")

include(":benchmark")
include(":feature:android-widgets")