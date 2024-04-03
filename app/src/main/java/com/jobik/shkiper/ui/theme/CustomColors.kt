package com.jobik.shkiper.ui.theme

import androidx.compose.ui.graphics.Color

val baseLightColorsHeightContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFFE2E2E2),
    container = Color(0xFFFFFFFF),
    border = Color(0xFFD2D2D2),
    text = Color(0xFF161616),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
)

val baseDarkColorsHeightContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(android.graphics.Color.parseColor("#0F0F0F")),
    container = Color(android.graphics.Color.parseColor("#191919")),
    border = Color(0xFF404040),
    text = Color(android.graphics.Color.parseColor("#FFFFFF")),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(android.graphics.Color.parseColor("#959595")),
)

val baseLightColorsLowContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(0xFFECEEEF),
    container = Color(0xFFFFFFFF),
    border = Color(0xFFD2D2D2),
    text = Color(0xFF161616),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF7A7A7A),
)

val baseDarkColorsLowContrast = CustomThemeColors(
    primary = Color(0xFF6D6AFF),
    background = Color(android.graphics.Color.parseColor("#1A1A1A")),
    container = Color(android.graphics.Color.parseColor("#242424")),
    border = Color(0xFF404040),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    onPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
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
    primary = Color(android.graphics.Color.parseColor("#4E9F3D")),
)

val DarkDeepGreen = baseDarkColorsHeightContrast.copy(
    primary = Color(android.graphics.Color.parseColor("#4E9F3D")),
)

/**
 * pastel green
 */
val LightPastelGreen = baseLightColorsLowContrast.copy(
    primary = Color(android.graphics.Color.parseColor("#42B883")),
)

val DarkPastelGreen = baseDarkColorsLowContrast.copy(
    primary = Color(android.graphics.Color.parseColor("#42B883")),
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
    primary = Color(android.graphics.Color.parseColor("#FF3040")),
)

val DarkDeepRed = baseDarkColorsHeightContrast.copy(
    primary = Color(android.graphics.Color.parseColor("#FF3040")),
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
    primary = Color(android.graphics.Color.parseColor("#FD8D14")),
)

val DarkDeepOrange = baseDarkColorsHeightContrast.copy(
    primary = Color(android.graphics.Color.parseColor("#FD8D14")),
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

