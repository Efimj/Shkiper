package com.jobik.shkiper.screens.advancedSettings

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.CustomSwitch
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.util.settings.SettingsManager
import com.jobik.shkiper.util.settings.SettingsState

@Composable
fun AdvancedSecureSettings() {
    SettingsGroup(header = stringResource(R.string.secure)) {
        SecureMode()
    }
}

@Composable
private fun SecureMode() {
    val context = LocalContext.current
    val settings = SettingsManager.settings.value

    fun switchSecureMode(
        settings: SettingsState?,
        context: Context
    ) {
        if (settings != null) {
            SettingsManager.update(
                context = context,
                settings = settings.copy(secureMode = settings.secureMode.not())
            )
        }
    }

    SettingsItem(
        icon = Icons.Outlined.Security,
        title = stringResource(R.string.secure_mode),
        description = stringResource(R.string.secure_mode_description),
        onClick = { switchSecureMode(settings = settings, context = context) }
    ) {
        CustomSwitch(
            active = settings?.secureMode ?: false,
            onClick = { switchSecureMode(settings = settings, context = context) }
        )
    }
}