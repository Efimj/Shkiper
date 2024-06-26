package com.jobik.shkiper.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ShkiperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: CustomThemeStyle = CustomThemeStyle.MaterialDynamicColors,
    content: @Composable () -> Unit
) {
    val colors = getThemeColors(style = style, darkTheme = darkTheme)

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = Transparent, darkIcons = darkTheme.not())
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

@Composable
private fun getThemeColors(
    style: CustomThemeStyle,
    darkTheme: Boolean
): CustomThemeColors {
    val colors = when {
        style == CustomThemeStyle.MaterialDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            getDynamicColors(darkTheme = darkTheme, context = LocalContext.current)

        darkTheme -> style.dark
        else -> style.light
    }
    return colors
}
