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

/**
 * deep purple
 */

val LightDeepPurple = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#433FFF")),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkDeepPurple = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#433FFF")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * pastel purple
 */

val LightPastelPurple = baseLightColors.copy(
    active = Color(0xFFB079FF),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkPastelPurple = baseDarkColors.copy(
    active = Color(0xFFB079FF),
    mainBackground = Color(0xFF1B1B1B),
    secondaryBackground = Color(0xFF272727),
    stroke = Color(0xFFE0E0E0),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * deep green
 */

val LightDeepGreen = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#4E9F3D")),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkDeepGreen = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#4E9F3D")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * pastel green
 */

val LightPastelGreen = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#42B883")),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkPastelGreen = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#42B883")),
    mainBackground = Color(android.graphics.Color.parseColor("#1A1A1A")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#242424")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * deep blue
 */

val LightDeepBlue = baseLightColors.copy(
    active = Color(0xFF23ACFF),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkDeepBlue = baseDarkColors.copy(
    active = Color(0xFF23ACFF),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * pastel blue
 */

val LightPastelBlue = baseLightColors.copy(
    active = Color(0xFF8AD3FF),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkPastelBlue = baseDarkColors.copy(
    active = Color(0xFF8AD3FF),
    mainBackground = Color(0xFF161616),
    secondaryBackground = Color(0xFF1E1E1E),
    stroke = Color(android.graphics.Color.parseColor("#2B2B2B")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#A1A1A1")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * deep red
 */

val LightDeepRed = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#FF3040")),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkDeepRed = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#FF3040")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * pastel red
 */

val LightPastelRed = baseLightColors.copy(
    active = Color(0xFFFF6B6B),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkPastelRed = baseDarkColors.copy(
    active = Color(0xFFFF7676),
    mainBackground = Color(android.graphics.Color.parseColor("#1A1A1A")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#242424")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * deep orange
 */

val LightDeepOrange = baseLightColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkDeepOrange = baseDarkColors.copy(
    active = Color(android.graphics.Color.parseColor("#FD8D14")),
    mainBackground = Color(android.graphics.Color.parseColor("#0F0F0F")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#191919")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

/**
 * pastel orange
 */

val LightPastelOrange = baseLightColors.copy(
    active = Color(0xFFFFB36B),
    mainBackground = Color(0xFFECEEEF),
    secondaryBackground = Color(0xFFFFFFFF),
    stroke = Color(0xFFE0E0E0),
    text = Color(0xFF161616),
    textOnActive = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DarkPastelOrange = baseDarkColors.copy(
    active = Color(0xFFFFB36B),
    mainBackground = Color(android.graphics.Color.parseColor("#1A1A1A")),
    secondaryBackground = Color(android.graphics.Color.parseColor("#242424")),
    stroke = Color(android.graphics.Color.parseColor("#404040")),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

