package com.efim.shkiper

import com.android.build.api.dsl.CommonExtension
import convention.src.main.kotlin.com.efim.shkiper.kotlinOptions
import convention.src.main.kotlin.src.efim.shkiper.libs
import org.gradle.api.Project

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("compose.compiler").get().toString()
        }

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" +
                        "${project.rootProject.projectDir.absolutePath}/compose_compiler_config.conf"
            )
        }
    }
}