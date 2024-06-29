package com.jobik.shkiper.screens.advancedSettings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotificationColor
import com.jobik.shkiper.database.models.NotificationIcon
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.settings.SettingsManager

@Composable
fun AdvancedNotificationSettings() {
    SettingsGroup(header = stringResource(R.string.Reminders)) {
        ReminderIcon()
        ReminderColor()
    }
}


@Composable
private fun ReminderIcon() {
    val context = LocalContext.current
    val settings = SettingsManager.settings

    SettingsItem(
        icon = Icons.Outlined.Notifications,
        title = stringResource(R.string.notification_defaults),
        onClick = { }
    ) {}
    Row(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.padding(bottom = 20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = NotificationIcon.entries, key = { it.name }) { icon ->
                val isSelected = settings.defaultNotificationIcon.name == icon.name

                val backgroundColor by
                animateColorAsState(targetValue = if (isSelected) AppTheme.colors.primary else Color.Transparent)

                val iconColor by
                animateColorAsState(targetValue = if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.text)

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable {
                            SettingsManager.update(
                                context = context,
                                settings = settings.copy(defaultNotificationIcon = icon)
                            )
                        }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(icon.getDrawable()),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(iconColor)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderColor() {
    val context = LocalContext.current
    val settings = SettingsManager.settings

    Row(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.padding(bottom = 15.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = NotificationColor.entries, key = { it.name }) { color ->
                val isSelected = settings.defaultNotificationColor.name == color.name

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            SettingsManager.update(
                                context = context,
                                settings = settings.copy(defaultNotificationColor = color)
                            )
                        }
                        .background(color.getColor(context)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = AppTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}