import com.android.build.api.dsl.LibraryExtension
import com.efim.shkiper.configureCompose
import convention.src.main.kotlin.src.efim.shkiper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UNUSED")
class ShkiperComposeLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(libs.findLibrary("androidx.material3").get())
                "implementation"(libs.findLibrary("androidx.material.icons.extended").get())
            }

            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }

            tasks.withType<KotlinCompile> {
                compilerOptions.freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true",
                )
            }
        }
    }
}