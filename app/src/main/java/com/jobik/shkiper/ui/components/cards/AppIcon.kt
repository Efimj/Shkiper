package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity

@Composable
fun AppIcon(
    activeIcon: LauncherActivity?,
    launcher: LauncherActivity,
    changeLauncher: (LauncherActivity) -> Unit
) {
    val isMaterial = launcher.name == LauncherActivity.Material.name
    val headerColor =
        animateColorAsState(
            targetValue = if (activeIcon != null && activeIcon.name == launcher.name) AppTheme.colors.primary else AppTheme.colors.text,
            label = "headerColor"
        )
    val borderColor =
        animateColorAsState(
            targetValue = if (activeIcon != null && activeIcon.name == launcher.name) AppTheme.colors.primary else Color.Transparent,
            label = "borderColor"
        )

    val primary = colorResource(id = R.color.primary)
    val onPrimary = colorResource(id = R.color.onPrimary)

    val colorFilter = if (isMaterial) ColorFilter.tint(onPrimary) else null
    val backgroundColor = if (isMaterial) primary else Color.White

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val shape = CircleShape
        Box(
            modifier = Modifier
                .border(width = 2.dp, color = borderColor.value, shape = shape)
                .size(60.dp)
                .clip(shape)
                .background(backgroundColor)
                .clickable { changeLauncher(launcher) },
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.padding(2.dp),
                painter = painterResource(id = launcher.drawable),
                contentDescription = null,
                colorFilter = colorFilter
            )
        }
        Text(
            modifier = Modifier.basicMarquee(),
            color = headerColor.value,
            text = stringResource(id = launcher.header),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}