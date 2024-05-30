import com.android.build.gradle.LibraryExtension
import convention.src.main.kotlin.com.efim.shkiper.configureKotlinAndroid
import convention.src.main.kotlin.src.efim.shkiper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("UNUSED")
class ShkiperLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.minSdk =
                    libs.findVersion("androidMinSdk").get().toString().toIntOrNull()
            }
        }
    }
}
