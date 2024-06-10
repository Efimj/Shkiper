package com.jobik.shkiper.screens.advancedSettings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.AppIcon
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.launch

@Composable
fun AdvancedSettings() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .allWindowInsetsPadding()
            .padding(top = 85.dp, bottom = 30.dp)
            .padding(horizontal = 10.dp)
    ) {
        SettingsGroup(header = stringResource(R.string.Application)) {
            SettingsItem(
                icon = Icons.Outlined.TouchApp,
                title = stringResource(R.string.application_icon)
            )
            AppIconSelector(context)
        }

    }
}

@Composable
private fun AppIconSelector(context: Context) {
    val activeIcon =
        remember { mutableStateOf(LauncherIcon().getEnabledLauncher(context = context)) }
    val scope = rememberCoroutineScope()
    val message = stringResource(id = R.string.application_icon_changed)

    val changeLauncher: (LauncherActivity) -> Unit = { launcher ->
        activeIcon.value = launcher
        LauncherIcon().switchLauncherIcon(
            context = context,
            activity = launcher
        )
        scope.launch {
            SnackbarHostUtil.snackbarHostState.showSnackbar(
                SnackbarVisualsCustom(
                    message = message,
                    icon = Icons.Outlined.TouchApp
                )
            )
        }
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = LauncherActivity.entries,
            key = { it.name }) { launcher ->

            AppIcon(activeIcon = activeIcon, launcher = launcher, changeLauncher = changeLauncher)
        }
    }
}