package com.jobik.shkiper.screens.advancedSettings

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrowserUpdated
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.CustomSwitch
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.SettingsItemColors
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.settings.SettingsManager
import com.jobik.shkiper.util.settings.SettingsState

@Composable
fun AdvancedUpdatesSettings() {
    SettingsGroup(header = stringResource(R.string.updates)) {
        AutomaticCheckUpdates()
        CheckUpdates()
    }
}

@Composable
private fun CheckUpdates() {
    SettingsItem(
        icon = Icons.Outlined.BrowserUpdated,
        title = stringResource(R.string.check_update),
        colors = SettingsItemColors(
            contentColor = AppTheme.colors.onSecondaryContainer,
            containerColor = AppTheme.colors.secondaryContainer,
            leadingIconColor = AppTheme.colors.onSecondaryContainer
        )
    ) {}
}

@Composable
private fun AutomaticCheckUpdates() {
    val settings = SettingsManager.settings.value
    val context = LocalContext.current

    fun switchAutomaticUpdateCheck(
        settings: SettingsState?,
        context: Context
    ) {
        if (settings != null) {
            SettingsManager.update(
                context = context,
                settings = settings.copy(checkUpdates = settings.checkUpdates.not())
            )
        }
    }

    SettingsItem(
        icon = Icons.Outlined.CloudDownload,
        title = stringResource(R.string.automatic_check),
        description = stringResource(R.string.suggests_updates),
        onClick = { switchAutomaticUpdateCheck(settings = settings, context = context) }
    ) {
        CustomSwitch(
            active = settings?.checkUpdates ?: false,
            onClick = {
                switchAutomaticUpdateCheck(settings = settings, context = context)
            })
    }
}

