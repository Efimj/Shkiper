package com.example.notepadapp.ui.theme

sealed class ColorTheme(
    val lightColors: ExtendedColors,
    val darkColors: ExtendedColors
) {
    object DefaultColorTheme : ColorTheme(DefaultLightThemeColors, DefaultDarkThemeColors)

    object ThemeList {
        val PageList = listOf(DefaultColorTheme)
        val Count = PageList.size
    }
}