package com.jobik.shkiper.ui.theme

data class ColorTheme(
    val lightColors: ExtendedColors,
    val darkColors: ExtendedColors
)

enum class ColorThemes(val colorTheme: ColorTheme) {
    Default(ColorTheme(DefaultLightColors, DefaultDarkColors)),
    VueTheme(ColorTheme(VueLightColors, VueDarkColors)),
    TelegramTheme(ColorTheme(TelegramLightColors, TelegramDarkColors)),
    LaravelTheme(ColorTheme(LaravelLightColors, LaravelDarkColors)),
    InstagramTheme(ColorTheme(InstagramLightColors, InstagramDarkColors)),
}