package com.jobik.shkiper.ui.theme

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class CustomThemeColors(
    val active: Color,
    val mainBackground: Color,
    val secondaryBackground: Color,
    val text: Color,
    val textOnActive: Color,
    val textSecondary: Color,
    val stroke: Color,
    val secondaryStroke: Color,
    val modalBackground: Color,
)

data class CustomThemeShapes(
    val none: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
)

object CustomTheme {
    val colors: CustomThemeColors
        @Composable
        get() = LocalCustomThemeColors.current
    val shapes: CustomThemeShapes
        @Composable
        get() = LocalCustomThemeShapes.current
}

@Keep
enum class CustomThemeStyle(val dark: CustomThemeColors, val light: CustomThemeColors) {
    DarkPurple(dark = DefaultDarkColors, light = DefaultLightColors),
    DarkGreen(dark = GreenDarkColors, light = GreenLightColors),
    LightGreen(dark = VueDarkColors, light = VueLightColors),
    DarkBlue(dark = TelegramDarkColors, light = TelegramLightColors),
    LightBlue(dark = BlueDarkColors, light = BlueLightColors),
    DarkRed(dark = InstagramDarkColors, light = InstagramLightColors),
    LightRed(dark = LaravelDarkColors, light = LaravelLightColors),
    DarkOrange(dark = OrangeDarkColors, light = OrangeLightColors),
    LightOrange(dark = OrangeFlatDarkColors, light = OrangeFlatLightColors);

    fun getColors(isDarkTheme: Boolean): CustomThemeColors {
        return if (isDarkTheme) dark else light
    }
}

val LocalCustomThemeColors = staticCompositionLocalOf<CustomThemeColors> {
    error("No colors provided")
}

val LocalCustomThemeShapes = staticCompositionLocalOf<CustomThemeShapes> {
    error("No shapes provided")
}