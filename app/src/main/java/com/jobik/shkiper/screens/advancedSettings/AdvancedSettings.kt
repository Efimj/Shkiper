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
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.jobik.shkiper.ui.components.fields.CustomSlider
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.CoroutineScope
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
            Column {
                SettingsItem(
                    icon = Icons.Outlined.TouchApp,
                    title = stringResource(R.string.application_icon)
                )
                AppIconSelector(context)
            }
            Column {
                SettingsItem(
                    icon = Icons.Outlined.TextFields,
                    title = stringResource(R.string.font_scale)
                )
                CustomSlider(value = 0.4f, onValueChange = { })
            }
        }
    }
}