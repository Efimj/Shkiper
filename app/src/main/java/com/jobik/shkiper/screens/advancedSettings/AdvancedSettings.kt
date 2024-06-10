package com.jobik.shkiper.screens.advancedSettings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon

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
            SettingsItem(title = stringResource(R.string.application_icon))
            LazyRow {
                items(
                    items = LauncherIcon.LauncherActivity.entries,
                    key = { it.name }) { launcher ->
                    Box(
                        Modifier.clickable {
                            LauncherIcon().switchLauncherIcon(
                                context = context,
                                activity = launcher
                            )
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(60.dp),
                            painter = painterResource(id = launcher.drawable),
                            contentDescription = null
                        )
                    }
                }
            }
        }

    }
}