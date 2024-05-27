package convention.src.main.kotlin

import com.android.build.gradle.LibraryExtension
import convention.src.main.kotlin.com.efim.shkiper.configureDetekt
import convention.src.main.kotlin.com.efim.shkiper.configureKotlinAndroid
import convention.src.main.kotlin.src.efim.shkiper.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UNUSED")
class ShkiperLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")

                apply(
                    libs.findLibrary("detekt-gradle").get().get().group
                )
            }

            configureDetekt(extensions.getByType<DetektExtension>())

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.minSdk =
                    libs.findVersion("androidMinSdk").get().toString().toIntOrNull()
            }

            dependencies {
                "implementation"(libs.findLibrary("androidxCore").get())
            }
        }
    }
}
