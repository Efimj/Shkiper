package com.example.notepadapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*

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

val LightThemeColors = ExtendedColors(
    mainBackground = light_background,
    stroke = light_stroke,
    text = light_text,
    textSecondary = light_text_secondary,
    modalBackground = light_modal_background,
    active = light_active,
    secondaryBackground = light_secondary_background,
)

val DarkThemeColors = ExtendedColors(
    mainBackground = dark_background,
    stroke = dark_stroke,
    text = dark_text,
    textSecondary = dark_text_secondary,
    modalBackground = dark_modal_background,
    active = dark_active,
    secondaryBackground = dark_secondary_background,
)

object CustomAppTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
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

    CompositionLocalProvider(LocalExtendedColors provides colors) {
        MaterialTheme(
            colors = mdColors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
