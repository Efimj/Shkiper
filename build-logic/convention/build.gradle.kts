import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.efim.shkiper.buildlogic"

// Configure the build-logic plugins to target JDK
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
            id = "efim.shkiper.hilt"
            implementationClass = "ShkiperLibraryHiltPlugin"
        }
        register("shkiperLibraryCompose") {
            id = "efim.shkiper.compose"
            implementationClass = "ShkiperComposeLibraryPlugin"
        }
    }
}
