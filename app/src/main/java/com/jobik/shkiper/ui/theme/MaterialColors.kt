package com.jobik.shkiper.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color

@RequiresApi(Build.VERSION_CODES.S)
fun getDynamicColors(context: Context, darkTheme: Boolean): CustomThemeColors {
    val dynamicPalette = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    return CustomThemeColors(
        primary = dynamicPalette.primary,
        onPrimary = dynamicPalette.onPrimary,
        secondaryContainer = dynamicPalette.secondaryContainer,
        onSecondaryContainer = dynamicPalette.onSecondaryContainer,
        background = dynamicPalette.background,
        text = dynamicPalette.onSurface,
        textSecondary = dynamicPalette.onSurface.copy(alpha = .6f),
        border = dynamicPalette.outline,
        container = dynamicPalette.surfaceContainerHigh,
    )
}

val M3YouLightPalette = CustomThemeColors(
    primary = Color(0xFF64558F),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEEDBFF),
    onSecondaryContainer = Color(0xFF260D43),
    background = Color(0xFFFDF7FF),
    text = Color(0xFF1C1B20),
    textSecondary = Color(0xFF1C1B20).copy(alpha = .6f),
    border = Color(0xFF79757F),
    container = Color(0xFFECE6EE),
)

val M3YouDarkPalette = CustomThemeColors(
    primary = Color(0xFFCEBDFE),
    onPrimary = Color(0xFF35275D),
    secondaryContainer = Color(0xFF543B72),
    onSecondaryContainer = Color(0xFFEEDBFF),
    background = Color(0xFF141318),
    text = Color(0xFFE6E1E9),
    textSecondary = Color(0xFFE6E1E9).copy(alpha = .6f),
    border = Color(0xFF938F99),
    container = Color(0xFF2B292F),
)

val M3LightPurple2Palette = CustomThemeColors(
    primary = Color(0xFF64558F),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEEDBFF),
    onSecondaryContainer = Color(0xFF260D43),
    background = Color(0xFFFDF7FF),
    text = Color(0xFF1C1B20),
    textSecondary = Color(0xFF1C1B20).copy(alpha = .6f),
    border = Color(0xFF79757F),
    container = Color(0xFFECE6EE),
)

val M3DarkPurple2Palette = CustomThemeColors(
    primary = Color(0xFFCEBDFE),
    onPrimary = Color(0xFF35275D),
    secondaryContainer = Color(0xFF543B72),
    onSecondaryContainer = Color(0xFFEEDBFF),
    background = Color(0xFF141318),
    text = Color(0xFFE6E1E9),
    textSecondary = Color(0xFFE6E1E9).copy(alpha = .6f),
    border = Color(0xFF938F99),
    container = Color(0xFF2B292F),
)

val M3LightBluePalette = CustomThemeColors(
    primary = Color(0xFF2B638B),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD4E4F6),
    onSecondaryContainer = Color(0xFF0D1D2A),
    background = Color(0xFFF7F9FF),
    text = Color(0xFF181C20),
    textSecondary = Color(0xFF181C20).copy(alpha = .6f),
    border = Color(0xFF72787E),
    container = Color(0xFFE6E8EE),
)

val M3DarkBluePalette = CustomThemeColors(
    primary = Color(0xFF98CCF9),
    onPrimary = Color(0xFF003351),
    secondaryContainer = Color(0xFF394857),
    onSecondaryContainer = Color(0xFFD4E4F6),
    background = Color(0xFF101418),
    text = Color(0xFFE0E2E8),
    textSecondary = Color(0xFFE0E2E8).copy(alpha = .6f),
    border = Color(0xFF8C9198),
    container = Color(0xFF272A2E),
)

val M3LightGreenPalette = CustomThemeColors(
    primary = Color(0xFF3D6838),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E8CE),
    onSecondaryContainer = Color(0xFF111F0F),
    background = Color(0xFFF7FBF1),
    text = Color(0xFF191D17),
    textSecondary = Color(0xFF191D17).copy(alpha = .6f),
    border = Color(0xFF73796F),
    container = Color(0xFFE6E9E0),
)

val M3DarkGreenPalette = CustomThemeColors(
    primary = Color(0xFFA3D398),
    onPrimary = Color(0xFF0D380D),
    secondaryContainer = Color(0xFF3C4B38),
    onSecondaryContainer = Color(0xFFD6E8CE),
    background = Color(0xFF10140F),
    text = Color(0xFFE0E4DA),
    textSecondary = Color(0xFFE0E4DA).copy(alpha = .6f),
    border = Color(0xFF8C9388),
    container = Color(0xFF272B25),
)

val M3LightYellowPalette = CustomThemeColors(
    primary = Color(0xFF815512),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFCDEBC),
    onSecondaryContainer = Color(0xFF271905),
    background = Color(0xFFFFF8F4),
    text = Color(0xFF211A13),
    textSecondary = Color(0xFF211A13).copy(alpha = .6f),
    border = Color(0xFF827568),
    container = Color(0xFFF3E6DA),
)

val M3DarkYellowPalette = CustomThemeColors(
    primary = Color(0xFFF7BC70),
    onPrimary = Color(0xFF462A00),
    secondaryContainer = Color(0xFF57432B),
    onSecondaryContainer = Color(0xFFFCDEBC),
    background = Color(0xFF18120C),
    text = Color(0xFFEDE0D4),
    textSecondary = Color(0xFFEDE0D4).copy(alpha = .6f),
    border = Color(0xFF9C8E80),
    container = Color(0xFF302921),
)

val M3LightPeachPalette = CustomThemeColors(
    primary = Color(0xFF904B3F),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDAD4),
    onSecondaryContainer = Color(0xFF2C1511),
    background = Color(0xFFFFF8F6),
    text = Color(0xFF231918),
    textSecondary = Color(0xFF231918).copy(alpha = .6f),
    border = Color(0xFF857370),
    container = Color(0xFFF7E4E1),
)

val M3DarkPeachPalette = CustomThemeColors(
    primary = Color(0xFFFFB4A7),
    onPrimary = Color(0xFF561E16),
    secondaryContainer = Color(0xFF5D3F3A),
    onSecondaryContainer = Color(0xFFFFDAD4),
    background = Color(0xFF1A1110),
    text = Color(0xFFF1DFDB),
    textSecondary = Color(0xFFF1DFDB).copy(alpha = .6f),
    border = Color(0xFFA08C89),
    container = Color(0xFF322826),
)

val M3LightPurplePalette = CustomThemeColors(
    primary = Color(0xFF595992),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2E0F9),
    onSecondaryContainer = Color(0xFF1A1A2C),
    background = Color(0xFFFCF8FF),
    text = Color(0xFF1B1B21),
    textSecondary = Color(0xFF1B1B21).copy(alpha = .6f),
    border = Color(0xFF777680),
    container = Color(0xFFEAE7EF),
)

val M3DarkPurplePalette = CustomThemeColors(
    primary = Color(0xFFC2C1FF),
    onPrimary = Color(0xFF2A2A60),
    secondaryContainer = Color(0xFF454559),
    onSecondaryContainer = Color(0xFFE2E0F9),
    background = Color(0xFF131318),
    text = Color(0xFFE4E1E9),
    textSecondary = Color(0xFFE4E1E9).copy(alpha = .6f),
    border = Color(0xFF918F9A),
    container = Color(0xFF2A292F),
)

val M3LightNauticalPalette = CustomThemeColors(
    primary = Color(0xFF006A66),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E5),
    onSecondaryContainer = Color(0xFF051F1E),
    background = Color(0xFFF4FBF9),
    text = Color(0xFF161D1C),
    textSecondary = Color(0xFF161D1C).copy(alpha = .6f),
    border = Color(0xFF6F7978),
    container = Color(0xFFE3E9E8),
)

val M3DarkNauticalPalette = CustomThemeColors(
    primary = Color(0xFF80D5CF),
    onPrimary = Color(0xFF003735),
    secondaryContainer = Color(0xFF324B49),
    onSecondaryContainer = Color(0xFFCCE8E5),
    background = Color(0xFF0E1514),
    text = Color(0xFFDDE4E2),
    textSecondary = Color(0xFFDDE4E2).copy(alpha = .6f),
    border = Color(0xFF889391),
    container = Color(0xFF252B2A),
)

val M3LightPinkPalette = CustomThemeColors(
    primary = Color(0xFF8C4A5E),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9E1),
    onSecondaryContainer = Color(0xFF2B151B),
    background = Color(0xFFFFF8F8),
    text = Color(0xFF22191B),
    textSecondary = Color(0xFF22191B).copy(alpha = .6f),
    border = Color(0xFF837376),
    container = Color(0xFFF5E4E7),
)

val M3DarkPinkPalette = CustomThemeColors(
    primary = Color(0xFFFFB1C6),
    onPrimary = Color(0xFF551D30),
    secondaryContainer = Color(0xFF5B3F46),
    onSecondaryContainer = Color(0xFFFFD9E1),
    background = Color(0xFF191113),
    text = Color(0xFFEFDFE1),
    textSecondary = Color(0xFFEFDFE1).copy(alpha = .6f),
    border = Color(0xFF9E8C90),
    container = Color(0xFF31282A),
)

/**
 * *************************************************************************
 */

val M3LightBlue2Palette = CustomThemeColors(
    primary = Color(0xFF39608F),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFC8E6FF),
    onSecondaryContainer = Color(0xFF001E2E),
    background = Color(0xFFF8F9FF),
    text = Color(0xFF191C20),
    textSecondary = Color(0xFF191C20).copy(alpha = .6f),
    border = Color(0xFF73777F),
    container = Color(0xFFE7E8EE),
)

val M3DarkBlue2Palette = CustomThemeColors(
    primary = Color(0xFFA3C9FE),
    onPrimary = Color(0xFF00315B),
    secondaryContainer = Color(0xFF19284E),
    onSecondaryContainer = Color(0xFFC8E6FF),
    background = Color(0xFF111418),
    text = Color(0xFFE1E2E8),
    textSecondary = Color(0xFFE1E2E8).copy(alpha = .6f),
    border = Color(0xFF8D9199),
    container = Color(0xFF272A2F),
)

val M3LightGreen2Palette = CustomThemeColors(
    primary = Color(0xFF3A693B),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFC9EEA7),
    onSecondaryContainer = Color(0xFF0C2000),
    background = Color(0xFFF7FBF1),
    text = Color(0xFF181D17),
    textSecondary = Color(0xFF181D17).copy(alpha = .6f),
    border = Color(0xFF72796F),
    container = Color(0xFFE6E9E0),
)

val M3DarkGreen2Palette = CustomThemeColors(
    primary = Color(0xFF9FD49B),
    onPrimary = Color(0xFF073911),
    secondaryContainer = Color(0xFF314E19),
    onSecondaryContainer = Color(0xFFC9EEA7),
    background = Color(0xFF10140F),
    text = Color(0xFFE0E4DB),
    textSecondary = Color(0xFFE0E4DB).copy(alpha = .6f),
    border = Color(0xFF8C9388),
    container = Color(0xFF272B25),
)

val M3LightYellow2Palette = CustomThemeColors(
    primary = Color(0xFF8D4E2C),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDDAF),
    onSecondaryContainer = Color(0xFF281800),
    background = Color(0xFFFFF8F6),
    text = Color(0xFF221A16),
    textSecondary = Color(0xFF221A16).copy(alpha = .6f),
    border = Color(0xFF85736C),
    container = Color(0xFFF6E5DD),
)

val M3DarkYellow2Palette = CustomThemeColors(
    primary = Color(0xFFFFB692),
    onPrimary = Color(0xFF542103),
    secondaryContainer = Color(0xFF4E3A2F),
    onSecondaryContainer = Color(0xFFFFDDAF),
    background = Color(0xFF1A120E),
    text = Color(0xFFF0DFD8),
    textSecondary = Color(0xFFF0DFD8).copy(alpha = .6f),
    border = Color(0xFFA08D85),
    container = Color(0xFF322823),
)

val M3LightPeach2Palette = CustomThemeColors(
    primary = Color(0xFF904A4B),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDAD4),
    onSecondaryContainer = Color(0xFF3A0A04),
    background = Color(0xFFFFF8F7),
    text = Color(0xFF221919),
    textSecondary = Color(0xFF221919).copy(alpha = .6f),
    border = Color(0xFF857372),
    container = Color(0xFFF6E4E3),
)

val M3DarkPeach2Palette = CustomThemeColors(
    primary = Color(0xFFFFB3B2),
    onPrimary = Color(0xFF561D20),
    secondaryContainer = Color(0xFF733429),
    onSecondaryContainer = Color(0xFFFFDAD4),
    background = Color(0xFF1A1111),
    text = Color(0xFFF0DEDE),
    textSecondary = Color(0xFFF0DEDE).copy(alpha = .6f),
    border = Color(0xFFA08C8C),
    container = Color(0xFF322827),
)

val M3LightNautical2Palette = CustomThemeColors(
    primary = Color(0xFF1D6586),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFB8E6FF),
    onSecondaryContainer = Color(0xFF00201F),
    background = Color(0xFFF6FAFE),
    text = Color(0xFF181C1F),
    textSecondary = Color(0xFF181C1F).copy(alpha = .6f),
    border = Color(0xFF71787E),
    container = Color(0xFFE5E8ED),
)

val M3DarkNautical2Palette = CustomThemeColors(
    primary = Color(0xFF90CEF4),
    onPrimary = Color(0xFF00344A),
    secondaryContainer = Color(0xFF1D3142),
    onSecondaryContainer = Color(0xFF9CF1EC),
    background = Color(0xFF0F1417),
    text = Color(0xFFDFE3E7),
    textSecondary = Color(0xFFDFE3E7).copy(alpha = .6f),
    border = Color(0xFF8B9297),
    container = Color(0xFF262B2E),
)

val M3LightPink2Palette = CustomThemeColors(
    primary = Color(0xFF874B6D),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9E0),
    onSecondaryContainer = Color(0xFF3A071A),
    background = Color(0xFFFFF8F8),
    text = Color(0xFF211A1D),
    textSecondary = Color(0xFF211A1D).copy(alpha = .6f),
    border = Color(0xFF827379),
    container = Color(0xFFF3E4E9),
)

val M3DarkPink2Palette = CustomThemeColors(
    primary = Color(0xFFFBB1D8),
    onPrimary = Color(0xFF511D3E),
    secondaryContainer = Color(0xFF703345),
    onSecondaryContainer = Color(0xFFFFD9E0),
    background = Color(0xFF181115),
    text = Color(0xFFEDDFE4),
    textSecondary = Color(0xFFEDDFE4).copy(alpha = .6f),
    border = Color(0xFF9C8D93),
    container = Color(0xFF30282B),
)

//val M3LightPalette = CustomThemeColors(
//
//)
//
//val M3DarkPalette = CustomThemeColors(
//
//)