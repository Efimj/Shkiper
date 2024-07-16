package com.jobik.shkiper.crash

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.ui.theme.ShkiperTheme
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.ContextUtils.adjustFontSize
import com.jobik.shkiper.util.settings.NightMode
import com.jobik.shkiper.util.settings.SettingsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class CrashActivity : CrashHandler() {
    override fun attachBaseContext(newBase: Context) {
        SettingsManager.init(newBase)
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = newBase,
                language = SettingsManager.settings.localization
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        adjustFontSize(SettingsManager.settings.fontScale)
        actionBar?.hide()

        setContent {
            ShkiperTheme(
                darkTheme = when (SettingsManager.settings.nightMode) {
                    NightMode.Light -> false
                    NightMode.Dark -> true
                    else -> isSystemInDarkTheme()
                },
                style = SettingsManager.settings.theme
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "DScas", color = AppTheme.colors.primary)
                }
            }
        }
    }
}