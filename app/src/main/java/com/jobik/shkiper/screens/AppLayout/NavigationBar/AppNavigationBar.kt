package com.jobik.shkiper.screens.AppLayout.NavigationBar

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

data class CustomBottomNavigationItem(
    val icon: ImageVector,
    @StringRes
    val description: Int,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

data class DefaultNavigationValues(
    val containerHeight: Dp = 58.dp
)

@Composable
fun CustomBottomNavigationItem(properties: CustomBottomNavigationItem) {
    val contentColorValue =
        if (properties.isSelected) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary
    val contentColor = animateColorAsState(targetValue = contentColorValue, label = "backgroundColor")

    val backgroundColorValue =
        if (properties.isSelected) AppTheme.colors.secondaryContainer else Color.Transparent
    val backgroundColor = animateColorAsState(targetValue = backgroundColorValue, label = "backgroundColor")

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.small)
            .background(backgroundColor.value)
            .clickable {
                properties.onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = properties.icon,
            contentDescription = stringResource(properties.description),
            tint = contentColor.value,
        )
    }
}

@Composable
fun CustomBottomNavigation(items: List<CustomBottomNavigationItem>) {
    Surface(
        shape = MaterialTheme.shapes.small,
        shadowElevation = 1.dp,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .clickable(enabled = false) {}
                .height(DefaultNavigationValues().containerHeight)
                .clip(shape = MaterialTheme.shapes.small)
                .background(AppTheme.colors.background)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.forEach {
                CustomBottomNavigationItem(properties = it)
            }
        }
    }
}