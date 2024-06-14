package com.jobik.shkiper.screens.advancedSettings

import android.app.Activity
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrowserUpdated
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.CustomSwitch
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.SettingsItemColors
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.ContextUtils.isInstalledFromPlayStore
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.settings.SettingsManager
import com.jobik.shkiper.util.settings.SettingsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdvancedUpdatesSettings() {
    SettingsGroup(header = stringResource(R.string.updates)) {
        AutomaticCheckUpdates()
        CheckUpdates()
    }
}

@Composable
private fun CheckUpdates() {
    val checkUpdate = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(checkUpdate.value) {
        if (checkUpdate.value) {
            if (context.isInstalledFromPlayStore()) {
                val appUpdateManager = AppUpdateManagerFactory.create(context)

                val appUpdateInfoTask = appUpdateManager.appUpdateInfo

                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    println(appUpdateInfo)
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        appUpdateManager.startUpdateFlow(
                            appUpdateInfo,
                            context as Activity,
                            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
                        )
                    } else {
                        scope.launch {
                            SnackbarHostUtil.snackbarHostState.showSnackbar(
                                SnackbarVisualsCustom(
                                    message = context.getString(R.string.latest_version_installed),
                                    icon = Icons.Outlined.CheckCircle
                                )
                            )
                        }
                    }
                }
            }else{
                scope.launch {
                    SnackbarHostUtil.snackbarHostState.showSnackbar(
                        SnackbarVisualsCustom(
                            message = context.getString(R.string.latest_version_installed),
                            icon = Icons.Outlined.CheckCircle
                        )
                    )
                }
            }
        }
    }

    SettingsItem(
        icon = Icons.Outlined.BrowserUpdated,
        title = stringResource(R.string.check_update),
        colors = SettingsItemColors(
            contentColor = AppTheme.colors.onSecondaryContainer,
            containerColor = AppTheme.colors.secondaryContainer,
            leadingIconColor = AppTheme.colors.onSecondaryContainer
        ),
        onClick = {
            checkUpdate.value = false
            scope.launch {
                delay(100)
                checkUpdate.value = true
            }
        }
    )
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

