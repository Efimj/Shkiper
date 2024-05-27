package convention.src.main.kotlin.com.efim.shkiper

import com.android.build.api.dsl.CommonExtension
import convention.src.main.kotlin.src.efim.shkiper.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.findVersion("androidCompileSdk").get().toString().toIntOrNull()

        defaultConfig {
            minSdk = libs.findVersion("androidMinSdk").get().toString().toIntOrNull()
        }

        flavorDimensions += "app"

        productFlavors {
            create("foss") {
                dimension = "app"
            }
            create("market") {
                dimension = "app"
            }
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures {
            compose = false
            aidl = false
            renderScript = false
            shaders = false
            buildConfig = false
            resValues = false
        }

        packaging {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }

        lint {
            disable += "UsingMaterialAndMaterial3Libraries"
            disable += "ModifierParameter"
        }

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                "-opt-in=androidx.compose.ui.unit.ExperimentalUnitApi",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
            jvmTarget = javaVersion.toString()
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("desugaring").get())
    }
}

val Project.javaVersion: JavaVersion
    get() = JavaVersion.toVersion(
        libs.findVersion("jvmTarget").get().toString()
    )

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}