package com.jobik.shkiper.ui.components.buttons

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun CustomSwitch(active: Boolean, onClick: (Boolean) -> Unit, thumbContent: @Composable() (() -> Unit)? = null) {
    Switch(
        checked = active,
        onCheckedChange = onClick,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colors.primary,
            checkedTrackColor = AppTheme.colors.secondaryContainer,
            checkedIconColor = AppTheme.colors.text,
            uncheckedThumbColor = AppTheme.colors.textSecondary,
            uncheckedTrackColor = AppTheme.colors.background,
            uncheckedIconColor = AppTheme.colors.primary,
            uncheckedBorderColor = AppTheme.colors.textSecondary
        )
    )
}