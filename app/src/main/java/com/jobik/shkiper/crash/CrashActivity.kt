package com.jobik.shkiper.crash

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jobik.shkiper.BuildConfig
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.ui.theme.ShkiperTheme
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.ContextUtils.adjustFontSize
import com.jobik.shkiper.util.settings.NightMode
import com.jobik.shkiper.util.settings.SettingsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrashActivity : CrashHandler() {
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

        val crashReason = getCrashReason()
        val exName = crashReason.split("\n\n")[0].trim()
        val ex = crashReason.split("\n\n").drop(1).joinToString("\n\n")

        val title = "[Bug] App Crash: $exName"
        val deviceInfo =
            "Device: ${Build.MODEL} (${Build.BRAND} - ${Build.DEVICE}), SDK: ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE}), App: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n\n"
        val body = "$deviceInfo$ex"

        setContent {
            ShkiperTheme(
                darkTheme = when (SettingsManager.settings.nightMode) {
                    NightMode.Light -> false
                    NightMode.Dark -> true
                    else -> isSystemInDarkTheme()
                },
                style = SettingsManager.settings.theme
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .background(AppTheme.colors.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = exName, color = AppTheme.colors.primary)
                    Text(text = ex, color = AppTheme.colors.text)
                    Text(text = title, color = AppTheme.colors.text)
                    Text(text = deviceInfo, color = AppTheme.colors.text)
                    Text(text = body, color = AppTheme.colors.text)

                }
            }
        }
    }
}