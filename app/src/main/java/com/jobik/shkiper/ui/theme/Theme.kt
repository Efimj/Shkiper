package com.jobik.shkiper.ui.theme

import android.app.Activity
import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.util.ThemeUtil

@Composable
fun ShkiperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: CustomThemeStyle = CustomThemeStyle.PastelPurple,
    content: @Composable () -> Unit
) {
    val colors = ThemeUtil.getColors(isDark = darkTheme, style = style)

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = colors.secondaryBackground
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme.not()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkTheme.not()
            window.navigationBarColor = Color.TRANSPARENT
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
