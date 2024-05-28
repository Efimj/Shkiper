import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.efim.shkiper.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
val javaVersion = JavaVersion.toVersion(libs.versions.jvmTarget.get())

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
}

dependencies {
    compileOnly(libs.agp.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.detekt.gradle)
}


gradlePlugin {
    // register the convention plugin
    plugins {
        register("shkiperLibraryPlugin") {
            id = "efim.shkiper.library"
            implementationClass = "ShkiperLibraryPlugin"
        }
        register("shkiperLibraryHilt") {
            id = "shkiper.hilt"
            implementationClass = "ShkiperLibraryHiltPlugin"
        }
    }
}
