package com.jobik.shkiper.ui.components.buttons

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun CustomSwitch(active: Boolean, onClick: (Boolean) -> Unit, thumbContent: @Composable() (() -> Unit)?) {
    Switch(
        checked = active,
        onCheckedChange = onClick,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = AppTheme.colors.primary,
            checkedIconColor = Color.DarkGray,
            uncheckedThumbColor = AppTheme.colors.textSecondary,
            uncheckedTrackColor = AppTheme.colors.background,
            uncheckedIconColor = AppTheme.colors.background,
            uncheckedBorderColor = AppTheme.colors.textSecondary

        )
    )
}