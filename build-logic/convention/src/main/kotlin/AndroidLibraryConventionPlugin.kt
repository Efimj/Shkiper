package convention.src.main.kotlin

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }
        }
    }

    private fun Project.configureKotlinAndroid(
        commonExtension: CommonExtension<*, *, *, *, *>,
    ) {
        commonExtension.apply {
            compileSdk = 34

            defaultConfig {
                minSdk = 26
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        configureKotlin()
    }

    private fun Project.configureKotlin() {
        // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                // Set JVM target to 11
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }
}
