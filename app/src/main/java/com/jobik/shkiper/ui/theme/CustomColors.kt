package com.jobik.shkiper.ui.theme

import androidx.compose.ui.graphics.Color

val baseLightColors = CustomThemeColors(
    active = Color(0xFF4284f5),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFD2D2D2),
    secondaryStroke = Color(0xFFD2D2D2),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val baseDarkColors = CustomThemeColors(
    active = Color(0xFF4284f5),
    mainBackground = Color(0xFF0F0F0F),
    secondaryBackground = Color(0xFF191919),
    stroke = Color(0xFF404040),
    secondaryStroke = Color(0xFF404040),
    text = Color(0xFFFFFFFF),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF959595),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val DefaultLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#433FFF")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    stroke = Color(android.graphics.Color.parseColor("#D2D2D2")),
    text = Color(android.graphics.Color.parseColor("#161616")),
    textSecondary = Color(android.graphics.Color.parseColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DefaultDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#433FFF")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val VueLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#42B883")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#f9f9f9")),
    stroke = Color(android.graphics.Color.parseColor("#f9f9f9")),
    text = Color(android.graphics.Color.parseColor("#213547")),
    textSecondary = Color(60, 60, 60, (255 * 0.7).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val VueDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#42B883")),
    mainBackground = Color(android.graphics.Color.parseColor("#1A1A1A")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#242424")),
    stroke = Color(android.graphics.Color.parseColor("#242424")),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val GreenLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#4E9F3D")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    stroke = Color(android.graphics.Color.parseColor("#D2D2D2")),
    text = Color(android.graphics.Color.parseColor("#161616")),
    textSecondary = Color(android.graphics.Color.parseColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val GreenDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#4E9F3D")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val BlueLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#50ABD6")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    stroke = Color(android.graphics.Color.parseColor("#D2D2D2")),
    text = Color(android.graphics.Color.parseColor("#161616")),
    textSecondary = Color(android.graphics.Color.parseColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val BlueDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#50ABD6")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val TelegramLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#2AABEE")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#f9f9f9")),
    stroke = Color(android.graphics.Color.parseColor("#f9f9f9")),
    text = Color(android.graphics.Color.parseColor("#000000")),
    textSecondary = Color(android.graphics.Color.parseColor("#878C8E")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val TelegramDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#2AABEE")),
    mainBackground = Color(android.graphics.Color.parseColor("#212121")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#2B2B2B")),
    stroke = Color(android.graphics.Color.parseColor("#2B2B2B")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#A1A1A1")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val LaravelLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#F9322C")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#F8F8FB")),
    stroke = Color(android.graphics.Color.parseColor("#1B1B1B")),
    text = Color(android.graphics.Color.parseColor("#000000")),
    textSecondary = Color(android.graphics.Color.parseColor("#585656")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val LaravelDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#F9322C")),
    mainBackground = Color(android.graphics.Color.parseColor("#171923")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#14161F")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#E7E8EC")),
    textSecondary = Color(android.graphics.Color.parseColor("#B5B5BD")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val InstagramLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#FF3040")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    stroke = Color(android.graphics.Color.parseColor("#DBDBDB")),
    text = Color(android.graphics.Color.parseColor("#000000")),
    textSecondary = Color(android.graphics.Color.parseColor("#737373")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val InstagramDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#FF3040")),
    mainBackground = Color(android.graphics.Color.parseColor("#000000")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#000000")),
    stroke = Color(android.graphics.Color.parseColor("#262626")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#A8A8A8")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val OrangeLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    stroke = Color(android.graphics.Color.parseColor("#D2D2D2")),
    text = Color(android.graphics.Color.parseColor("#161616")),
    textSecondary = Color(android.graphics.Color.parseColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val OrangeDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val OrangeFlatLightColors = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(android.graphics.Color.parseColor("#FFFFFF")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#F8F8F8")),
    stroke = Color(android.graphics.Color.parseColor("#1B1B1B")),
    text = Color(android.graphics.Color.parseColor("#161616")),
    textSecondary = Color(android.graphics.Color.parseColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val OrangeFlatDarkColors = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#1B1B1B")),
    stroke = Color(android.graphics.Color.parseColor("#1B1B1B")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

