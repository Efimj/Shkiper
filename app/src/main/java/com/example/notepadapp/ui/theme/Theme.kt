package com.example.notepadapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

private val LightColorPalette = lightColors(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
)

//@Composable
//fun AppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
//
//    MaterialTheme(
//        colors = colors,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
//}

val LightThemeColors by lazy {
    CustomColors(
        mainBackground = light_background,
        stroke = light_stroke,
        text = light_text,
        textSecondary = light_text_secondary,
    )
}

val DarkThemeColors by lazy {
    CustomColors(
        mainBackground = dark_background,
        stroke = dark_stroke,
        text = dark_text,
        textSecondary = dark_text_secondary,
    )
}

object CustomAppTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
//    val typography: Typography
//        @Composable
//        get() = MaterialTheme.typography
//
//    val shapes: Shapes
//        @Composable
//        get() = MaterialTheme.shapes
}

@Composable
fun CustomAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val mdColors = if (darkTheme) DarkColorPalette else LightColorPalette
    val colors = if (darkTheme) DarkThemeColors else LightThemeColors

    ProvideCustomColors(colors = colors) {
        MaterialTheme(
            colors = mdColors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
