package com.jobik.shkiper.screens.advancedSettings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.AppIcon
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.fields.CustomSlider
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.settings.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun AdvancedApplicationSettings(){
    SettingsGroup(header = stringResource(R.string.Application)) {
        AppIconSelector()
        FontScaleSettings()
    }
}

@Composable
private fun AppIconSelector() {
    val context = LocalContext.current

    val activeIcon =
        remember { mutableStateOf(LauncherIcon().getEnabledLauncher(context = context)) }
    val scope = rememberCoroutineScope()

    val messageIconChanged = stringResource(id = R.string.application_icon_changed)
    val messageIconAlreadySelected = stringResource(id = R.string.icon_already_selected)
    Column {
        SettingsItem(
            icon = Icons.Outlined.TouchApp,
            title = stringResource(R.string.application_icon)
        )
        LazyRow(
            modifier = Modifier.padding(bottom = 10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = LauncherActivity.entries,
                key = { it.name }) { launcher ->

                AppIcon(
                    activeIcon = activeIcon.value,
                    launcher = launcher,
                    changeLauncher = { newLauncher ->
                        selectNewIcon(
                            launcher = newLauncher,
                            activeIcon = activeIcon,
                            scope = scope,
                            messageIconAlreadySelected = messageIconAlreadySelected,
                            context = context,
                            messageIconChanged = messageIconChanged
                        )
                    }
                )
            }
        }
    }
}

private fun selectNewIcon(
    context: Context,
    launcher: LauncherActivity,
    activeIcon: MutableState<LauncherActivity?>,
    scope: CoroutineScope,
    messageIconAlreadySelected: String,
    messageIconChanged: String,
) {
    val selected = activeIcon.value
    if (selected != null && launcher.name == selected.name) {
        scope.launch {
            SnackbarHostUtil.snackbarHostState.showSnackbar(
                SnackbarVisualsCustom(
                    message = messageIconAlreadySelected,
                    icon = Icons.Outlined.TouchApp
                )
            )
        }
        return
    }
    activeIcon.value = launcher
    LauncherIcon().switchLauncherIcon(
        context = context,
        activity = launcher
    )
    scope.launch {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = messageIconChanged,
                icon = Icons.Outlined.TouchApp
            )
        )
    }
}

@Composable
private fun FontScaleSettings() {
    val context = LocalContext.current

    val fontScale = remember {
        mutableFloatStateOf(SettingsManager.settings.fontScale)
    }

    /**
     * Rounds this [Float] to another with 2 significant numbers
     * 0.1234 is rounded to 0.12
     * 0.127 is rounded to 0.13
     */
    fun Float.roundToTwoDigits() = (this * 100.0f).roundToInt() / 100.0f

    Column {
        SettingsItem(
            icon = Icons.Outlined.TextFields,
            title = stringResource(R.string.font_scale),
            action = {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%.2f",
                        fontScale.floatValue.roundToTwoDigits()
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.primary
                )
            }
        )
        CustomSlider(
            modifier = Modifier.padding(horizontal = 20.dp),
            value = fontScale.floatValue,
            valueRange = 0.5f..1.5f,
            onValueChange = { fontScale.floatValue = it },
            onValueChangeFinished = {
                val settings = SettingsManager.settings
                if (settings != null) {
                    SettingsManager.update(
                        context = context,
                        settings = settings.copy(fontScale = fontScale.floatValue.roundToTwoDigits())
                    )
                }
                (context as Activity).recreate()
            },
            steps = 19
        )
    }
}