package convention.src.main.kotlin

import convention.src.main.kotlin.src.efim.shkiper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UNUSED")
class ShkiperLibraryHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }
            dependencies {
                "implementation"(libs.findLibrary("hilt").get())
                "kapt"(libs.findLibrary("dagger.hilt.compiler").get())
            }
        }
    }
}