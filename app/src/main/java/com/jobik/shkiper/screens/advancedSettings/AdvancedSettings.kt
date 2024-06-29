package com.jobik.shkiper.screens.advancedSettings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme

@SuppressLint("DefaultLocale")
@Composable
fun AdvancedSettings() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .allWindowInsetsPadding()
            .padding(top = 85.dp, bottom = 30.dp)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AdvancedApplicationSettings()
        AdvancedNotificationSettings()
        AdvancedSecureSettings()
        AdvancedUpdatesSettings()
    }
}