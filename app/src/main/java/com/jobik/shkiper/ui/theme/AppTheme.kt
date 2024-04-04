package com.jobik.shkiper.ui.theme

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class CustomThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val background: Color,
    val container: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val text: Color,
    val textSecondary: Color,
    val border: Color,
)

data class CustomThemeShapes(
    val none: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
)

object AppTheme {
    val colors: CustomThemeColors
        @Composable
        get() = LocalCustomThemeColors.current
    val shapes: CustomThemeShapes
        @Composable
        get() = LocalCustomThemeShapes.current
}

@Keep
enum class CustomThemeStyle(val dark: CustomThemeColors, val light: CustomThemeColors) {
    DeepPurple(dark = DarkDeepPurple, light = LightDeepPurple),
    DeepRed(dark = DarkDeepRed, light = LightDeepRed),
    DeepOrange(dark = DarkDeepOrange, light = LightDeepOrange),
    DeepBlue(dark = DarkDeepBlue, light = LightDeepBlue),
    DeepGreen(dark = DarkDeepGreen, light = LightDeepGreen),

    PastelPurple(dark = DarkPastelPurple, light = LightPastelPurple),
    PastelRed(dark = DarkPastelRed, light = LightPastelRed),
    PastelOrange(dark = DarkPastelOrange, light = LightPastelOrange),
    PastelBlue(dark = DarkPastelBlue, light = LightPastelBlue),
    PastelGreen(dark = DarkPastelGreen, light = LightPastelGreen),

    M3Purple2(dark = M3DarkPurple2Palette, light = M3LightPurple2Palette),
    M3Blue(dark = M3DarkBluePalette, light = M3LightBluePalette),
    M3Green(dark = M3DarkGreenPalette, light = M3LightGreenPalette),
    M3Yellow(dark = M3DarkYellowPalette, light = M3LightYellowPalette),
    M3Peach(dark = M3DarkPeachPalette, light = M3LightPeachPalette),
    M3Purple(dark = M3DarkPurplePalette, light = M3LightPurplePalette),
    M3Nautical(dark = M3DarkNauticalPalette, light = M3LightNauticalPalette),
    M3Pink(dark = M3DarkPinkPalette, light = M3LightPinkPalette),
    M3Blue2(dark = M3DarkBlue2Palette, light = M3LightBlue2Palette),
    M3Green2(dark = M3DarkGreen2Palette, light = M3LightGreen2Palette),
    M3Yellow2(dark = M3DarkYellow2Palette, light = M3LightYellow2Palette),
    M3Peach2(dark = M3DarkPeach2Palette, light = M3LightPeach2Palette),
    M3Nautical2(dark = M3DarkNautical2Palette, light = M3LightNautical2Palette),
    M3Pink2(dark = M3DarkPink2Palette, light = M3LightPink2Palette);

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