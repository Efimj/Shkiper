package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity

@Composable
fun AppIcon(
    activeIcon: MutableState<LauncherActivity?>,
    launcher: LauncherActivity,
    changeLauncher: (LauncherActivity) -> Unit
) {
    val borderColor =
        animateColorAsState(
            targetValue = if (activeIcon.value != null && activeIcon.value!!.name == launcher.name) AppTheme.colors.primary else Color.Transparent,
            label = "borderColor"
        )

    Box(
        modifier = Modifier
            .border(width = 2.dp, color = borderColor.value, shape = CircleShape)
            .size(60.dp)
            .clip(CircleShape)
            .clickable { changeLauncher(launcher) },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = launcher.drawable),
            contentDescription = null
        )
    }
}