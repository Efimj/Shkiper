package com.jobik.shkiper.ui.theme

import androidx.compose.ui.graphics.Color

private val baseLightColorsHeightContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFFFFFFFF),
    container = Color(0xFFF2F2F2),
    border = Color(0xFFD2D2D2),
    text = Color(0xFF161616),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    secondaryContainer = Color(0xFFE3E3E3),
    onSecondaryContainer = Color(0xFF2C2C2C),
)

private val baseDarkColorsHeightContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFF0F0F0F),
    container = Color(0xFF191919),
    border = Color(0xFF404040),
    text = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF959595),
    secondaryContainer = Color(0xFF222222),
    onSecondaryContainer = Color(0xFFF1F1F1),
)

private val baseLightColorsLowContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFFFFFFFF),
    container = Color(0xFFF7F7F7),
    border = Color(0xFFD2D2D2),
    text = Color(0xFF161616),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
    secondaryContainer = Color(0xFFEDEDED),
    onSecondaryContainer = Color(0xFF2C2C2C),
)

private val baseDarkColorsLowContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFF1A1A1A),
    container = Color(0xFF242424),
    border = Color(0xFF404040),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    secondaryContainer = Color(0xFF2E2E2E),
    onSecondaryContainer = Color(0xFFF1F1F1),
)

/**
 * deep purple
 */
val LightDeepPurple = baseLightColorsHeightContrast.copy(
    primary = Color(0xFF6D6AFF),
)

val DarkDeepPurple = baseDarkColorsHeightContrast.copy(
    primary = Color(0xFF6D6AFF),
)

/**
 * pastel purple
 */
val LightPastelPurple = baseLightColorsLowContrast.copy(
    primary = Color(0xFFB079FF),
)

val DarkPastelPurple = baseDarkColorsLowContrast.copy(
    primary = Color(0xFFB079FF),
)

/**
 * deep green
 */
val LightDeepGreen = baseLightColorsHeightContrast.copy(
    primary = Color(0xFF4E9F3D),
)

val DarkDeepGreen = baseDarkColorsHeightContrast.copy(
    primary = Color(0xFF4E9F3D),
)

/**
 * pastel green
 */
val LightPastelGreen = baseLightColorsLowContrast.copy(
    primary = Color(0xFF42B883),
)

val DarkPastelGreen = baseDarkColorsLowContrast.copy(
    primary = Color(0xFF42B883),
)

/**
 * deep blue
 */
val LightDeepBlue = baseLightColorsHeightContrast.copy(
    primary = Color(0xFF5297FF),
)

val DarkDeepBlue = baseDarkColorsHeightContrast.copy(
    primary = Color(0xFF5297FF),
)

/**
 * pastel blue
 */
val LightPastelBlue = baseLightColorsLowContrast.copy(
    primary = Color(0xFF6DA8FF),
)

val DarkPastelBlue = baseDarkColorsLowContrast.copy(
    primary = Color(0xFF6DA8FF),
)

/**
 * deep red
 */
val LightDeepRed = baseLightColorsHeightContrast.copy(
    primary = Color(0xFFFF3040),
)

val DarkDeepRed = baseDarkColorsHeightContrast.copy(
    primary = Color(0xFFFF3040),
)

/**
 * pastel red
 */
val LightPastelRed = baseLightColorsLowContrast.copy(
    primary = Color(0xFFFF6B6B),
)

val DarkPastelRed = baseDarkColorsLowContrast.copy(
    primary = Color(0xFFFF7676),
)

/**
 * deep orange
 */
val LightDeepOrange = baseLightColorsHeightContrast.copy(
    primary = Color(0xFFFD8D14),
)

val DarkDeepOrange = baseDarkColorsHeightContrast.copy(
    primary = Color(0xFFFD8D14),
)

/**
 * pastel orange
 */
val LightPastelOrange = baseLightColorsLowContrast.copy(
    primary = Color(0xFFFFB36B),
)

val DarkPastelOrange = baseDarkColorsLowContrast.copy(
    primary = Color(0xFFFFB36B),
)

