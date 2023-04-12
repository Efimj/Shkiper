package com.example.notepadapp.ui.theme

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        mainBackground = Color.Unspecified,
        stroke = Color.Unspecified,
        text = Color.Unspecified,
        textSecondary = Color.Unspecified,
    )
}
