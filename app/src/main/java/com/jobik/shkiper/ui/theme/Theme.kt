package com.jobik.shkiper.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ShkiperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: CustomThemeStyle = CustomThemeStyle.PastelPurple,
    content: @Composable () -> Unit
) {
    val colors = when {
        style == CustomThemeStyle.MaterialDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            getDynamicColors(darkTheme = darkTheme, context = LocalContext.current)

        darkTheme -> style.dark
        else -> style.light
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme.not()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkTheme.not()
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    CompositionLocalProvider(
        LocalCustomThemeColors provides colors,
        LocalCustomThemeShapes provides CustomShapes,
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = MaterialShapes,
            content = content
        )
    }
}
