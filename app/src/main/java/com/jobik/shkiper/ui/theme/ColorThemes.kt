package com.jobik.shkiper.ui.theme

data class ColorTheme(
    val lightColors: ExtendedColors,
    val darkColors: ExtendedColors
)

enum class ColorThemes(val colorTheme: ColorTheme) {
    Default(ColorTheme(DefaultLightColors, DefaultDarkColors)),
    GreenTheme(ColorTheme(GreenLightColors, GreenDarkColors)),
    VueTheme(ColorTheme(VueLightColors, VueDarkColors)),
    BlueTheme(ColorTheme(BlueLightColors, BlueDarkColors)),
    TelegramTheme(ColorTheme(TelegramLightColors, TelegramDarkColors)),
    LaravelTheme(ColorTheme(LaravelLightColors, LaravelDarkColors)),
    InstagramTheme(ColorTheme(InstagramLightColors, InstagramDarkColors)),
    OrangeTheme(ColorTheme(OrangeLightColors, OrangeDarkColors)),
    OrangeFlatTheme(ColorTheme(OrangeFlatLightColors, OrangeFlatDarkColors)),
}