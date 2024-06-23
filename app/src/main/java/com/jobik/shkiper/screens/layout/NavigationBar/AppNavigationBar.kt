package com.jobik.shkiper.screens.layout.NavigationBar

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.helpers.px
import com.jobik.shkiper.ui.theme.AppTheme

data class CustomBottomNavigationItem(
    val icon: ImageVector,
    @StringRes val description: Int,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

data class DefaultNavigationValues(
    val containerHeight: Dp = 58.dp
)

@Composable
fun CustomBottomNavigationItem(properties: CustomBottomNavigationItem) {
    val contentColor = animateColorAsState(
        targetValue = if (properties.isSelected) AppTheme.colors.onPrimary else AppTheme.colors.onSecondaryContainer,
        label = "contentColor"
    )

    IconButton(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = contentColor.value,
        ),
        onClick = {
            properties.onClick()
        }
    ) {
        Icon(
            imageVector = properties.icon,
            contentDescription = stringResource(properties.description),
            tint = contentColor.value
        )
    }
}

@Composable
fun CustomBottomNavigation(items: List<CustomBottomNavigationItem>) {
    var lastIndex by remember { mutableIntStateOf(0) }
    val selectedIndex = items.indexOfFirst { it.isSelected }.let {
        if (it == -1) lastIndex else it
    }

    LaunchedEffect(selectedIndex) {
        lastIndex = if (selectedIndex == -1) {
            lastIndex
        } else {
            selectedIndex
        }
    }

    val containerPaddings = 4.dp
    val spacerBetween = 6.dp
    val buttonWidth = DefaultNavigationValues().containerHeight.px - containerPaddings.px * 2

    val indicatorOffset by animateIntAsState(
        targetValue = lastIndex * (buttonWidth + spacerBetween.px),
        label = ""
    )

    Surface(
        shape = CircleShape,
        shadowElevation = 1.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .height(DefaultNavigationValues().containerHeight)
                .clip(shape = CircleShape)
                .background(AppTheme.colors.secondaryContainer)
                .padding(containerPaddings),
        ) {
            // Background Indicator
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .offset { IntOffset(indicatorOffset, 0) }
                    .background(AppTheme.colors.primary, CircleShape)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacerBetween)
            ) {
                items.forEach {
                    CustomBottomNavigationItem(properties = it)
                }
            }
        }
    }
}
