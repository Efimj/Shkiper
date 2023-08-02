package com.jobik.shkiper.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        mainBackground = Color.Unspecified,
        stroke = Color.Unspecified,
        text = Color.Unspecified,
        textSecondary = Color.Unspecified,
        modalBackground = Color.Unspecified,
        active = Color.Unspecified,
        secondaryBackground = Color.Unspecified,
    )
}
