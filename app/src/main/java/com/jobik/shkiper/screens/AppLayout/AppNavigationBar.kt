package com.jobik.shkiper.screens.AppLayout

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
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
import com.jobik.shkiper.ui.theme.CustomTheme

data class CustomBottomNavigationItem(
    val icon: ImageVector,
    @StringRes
    val description: Int,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

data class DefaultNavigationValues(
    val buttonSize: Dp = 50.dp,
    val iconSize: Dp = 26.dp,
    val containerPaddings: Dp = 8.dp,
    val containerHeight: Dp = containerPaddings * 2 + buttonSize
    )

@Composable
fun CustomBottomNavigationItem(properties: CustomBottomNavigationItem) {
    val contentColorValue =
        if (properties.isSelected) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary
    val contentColor = animateColorAsState(targetValue = contentColorValue, label = "backgroundColor")

    val backgroundColorValue =
        if (properties.isSelected) CustomTheme.colors.active.copy(alpha = .5f) else Color.Transparent
    val backgroundColor = animateColorAsState(targetValue = backgroundColorValue, label = "backgroundColor")

    Row(
        modifier = Modifier
            .height(DefaultNavigationValues().buttonSize)
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
            modifier = Modifier
                .size(DefaultNavigationValues().iconSize)
        )
    }
}

@Composable
fun CustomBottomNavigation(items: List<CustomBottomNavigationItem>) {
    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .border(width = 1.dp, color = CustomTheme.colors.mainBackground, shape = MaterialTheme.shapes.small)
            .background(CustomTheme.colors.secondaryBackground)
            .padding(DefaultNavigationValues().containerPaddings),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach {
            CustomBottomNavigationItem(properties = it)
        }
    }
}