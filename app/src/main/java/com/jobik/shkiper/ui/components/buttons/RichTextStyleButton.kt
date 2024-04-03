package com.jobik.shkiper.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun RichTextStyleButton(
    isActive: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String = "",
) {
    Card(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .focusProperties { canFocus = false },
        elevation = 0.dp,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        backgroundColor = if (isActive) CustomTheme.colors.primary else Color.Transparent,
        contentColor = if (isActive) CustomTheme.colors.onPrimary else CustomTheme.colors.textSecondary,
    ) {
        Icon(
            modifier = Modifier.padding(2.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isActive) CustomTheme.colors.onPrimary else CustomTheme.colors.textSecondary
        )
    }
}

@Composable
fun RichTextStyleButton(
    isActive: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    contentDescription: String = "",
) {
    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (isActive) CustomTheme.colors.onPrimary else CustomTheme.colors.textSecondary,
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
        elevation = 0.dp,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        backgroundColor = buttonBackgroundColor,
        contentColor = buttonContentColor,
    ) {
        Icon(
            modifier = Modifier.padding(2.dp),
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = if (isActive) CustomTheme.colors.onPrimary else CustomTheme.colors.textSecondary
        )
    }
}