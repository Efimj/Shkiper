package com.jobik.shkiper.screens.advancedSettings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.AppIcon
import com.jobik.shkiper.util.LauncherIcon
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppIconSelector(context: Context) {
    val activeIcon =
        remember { mutableStateOf(LauncherIcon().getEnabledLauncher(context = context)) }
    val scope = rememberCoroutineScope()

    val messageIconChanged = stringResource(id = R.string.application_icon_changed)
    val messageIconAlreadySelected = stringResource(id = R.string.icon_already_selected)

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