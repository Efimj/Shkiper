package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.ThemeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    icon: ImageVector? = null,
    title: String,
    isEnabled: Boolean = true,
    isActive: Boolean = false,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: (() -> Unit) = {},
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
    action: (@Composable () -> Unit)? = null
) {
    val backgroundColor = if (isActive) CustomTheme.colors.active else CustomTheme.colors.secondaryBackground
    val foregroundColor = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.text
    val foregroundSecondaryColor = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary

    val containerColor = remember { Animatable(backgroundColor) }
    val contentColor = remember { Animatable(foregroundColor) }
    val contentSecondaryColor = remember { Animatable(foregroundSecondaryColor) }

    LaunchedEffect(isActive) {
        containerColor.animateTo(backgroundColor, animationSpec = tween(200))
    }

    LaunchedEffect(isActive) {
        contentColor.animateTo(foregroundColor, animationSpec = tween(200))
    }

    LaunchedEffect(isActive) {
        contentSecondaryColor.animateTo(foregroundSecondaryColor, animationSpec = tween(200))
    }

    LaunchedEffect(ThemeUtil.isDarkMode.value) {
        containerColor.snapTo(backgroundColor)
    }

    LaunchedEffect(ThemeUtil.isDarkMode.value) {
        contentColor.snapTo(foregroundColor)
    }

    LaunchedEffect(ThemeUtil.isDarkMode.value) {
        contentSecondaryColor.snapTo(foregroundSecondaryColor)
    }

    Card(onClick = onClick, enabled = isEnabled, shape = CustomTheme.shapes.none) {
        Row(
            modifier = modifier.fillMaxWidth().background(containerColor.value)
                .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon == null)
                Spacer(Modifier.padding(start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr)))
            if (icon !== null)
                Row(modifier = Modifier.padding(horizontal = contentPadding.calculateLeftPadding(LayoutDirection.Ltr))) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).fillMaxSize(),
                        tint = contentSecondaryColor.value
                    )
                }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = contentColor.value
                )
                if (description !== null)
                    Spacer(modifier = Modifier.height(3.dp))
                if (description !== null)
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = contentSecondaryColor.value
                    )
            }
            Row(
                modifier = Modifier.padding(horizontal = contentPadding.calculateRightPadding(LayoutDirection.Ltr)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (action !== null)
                    action()
            }
        }
    }
}