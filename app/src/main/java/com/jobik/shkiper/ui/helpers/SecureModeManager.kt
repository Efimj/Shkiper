package com.jobik.shkiper.ui.helpers

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.jobik.shkiper.util.settings.SettingsManager

@Composable
fun SecureModeManager(
    enabled: Boolean = SettingsManager.settings.value?.secureMode ?: false
) {
    val context = LocalContext.current as ComponentActivity

    LaunchedEffect(enabled) {
        if (enabled) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            context.window.clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }
}