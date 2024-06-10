package com.jobik.shkiper.ui.components.cards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.LauncherIcon.LauncherActivity

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppIcon(
    activeIcon: MutableState<LauncherActivity?>,
    launcher: LauncherActivity,
    changeLauncher: (LauncherActivity) -> Unit
) {
    val isMaterial = launcher.name == LauncherActivity.Material.name
    val borderColor =
        animateColorAsState(
            targetValue = if (activeIcon.value != null && activeIcon.value!!.name == launcher.name) AppTheme.colors.primary else Color.Transparent,
            label = "borderColor"
        )

    val primary = colorResource(id = R.color.primary)
    val onPrimary = colorResource(id = R.color.onPrimary)

    val colorFilter = if(isMaterial) ColorFilter.tint(onPrimary) else null
    val backgroundColor = if(isMaterial) primary else Color.Transparent

    Box(
        modifier = Modifier
            .border(width = 2.dp, color = borderColor.value, shape = CircleShape)
            .size(60.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { changeLauncher(launcher) },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = launcher.drawable),
            contentDescription = null,
            colorFilter = colorFilter
        )
    }
}