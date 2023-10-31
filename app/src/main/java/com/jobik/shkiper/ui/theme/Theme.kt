package com.jobik.shkiper.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.util.ThemeUtil

@Composable
fun ShkiperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: CustomThemeStyle = CustomThemeStyle.DarkPurple,
    content: @Composable () -> Unit
) {
    val colors = ThemeUtil.getColors(isDark = darkTheme, style = style)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colors.mainBackground.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = colors.secondaryBackground
    )

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
