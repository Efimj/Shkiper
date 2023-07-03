package com.example.notepadapp.ui.theme

data class ColorTheme(
    val lightColors: ExtendedColors,
    val darkColors: ExtendedColors
)

enum class ColorThemes(val colorTheme: ColorTheme) {
    Default(ColorTheme(DefaultLightThemeColors, DefaultDarkThemeColors))
}