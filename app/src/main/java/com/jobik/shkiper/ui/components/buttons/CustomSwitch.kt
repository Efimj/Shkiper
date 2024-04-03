package com.jobik.shkiper.ui.components.buttons

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun CustomSwitch(active: Boolean, onClick: (Boolean) -> Unit, thumbContent: @Composable() (() -> Unit)?) {
    Switch(
        checked = active,
        onCheckedChange = onClick,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = CustomTheme.colors.primary,
            checkedIconColor = Color.DarkGray,
            uncheckedThumbColor = CustomTheme.colors.textSecondary,
            uncheckedTrackColor = CustomTheme.colors.background,
            uncheckedIconColor = CustomTheme.colors.background,
            uncheckedBorderColor = CustomTheme.colors.textSecondary

        )
    )
}