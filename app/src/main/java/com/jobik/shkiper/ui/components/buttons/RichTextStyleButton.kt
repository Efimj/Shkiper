package com.jobik.shkiper.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomTheme
import androidx.compose.material3.*
import androidx.compose.ui.Alignment

@Composable
fun RichTextStyleButton(
    isActive: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    contentDescription: String = "",
) {
    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (isActive) CustomTheme.colors.onPrimary else CustomTheme.colors.onSecondaryContainer,
        label = "buttonContentColor"
    )

    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (isActive) CustomTheme.colors.primary else Color.Transparent,
        label = "buttonBackgroundColor"
    )

    Card(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable(onClick = onClick)
            .focusProperties { canFocus = false },
        shape = RoundedCornerShape(5.dp),
        border = null,
        colors = CardDefaults.cardColors(contentColor = buttonContentColor, containerColor = buttonBackgroundColor),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.padding(2.dp),
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                tint = buttonContentColor
            )
        }
    }
}