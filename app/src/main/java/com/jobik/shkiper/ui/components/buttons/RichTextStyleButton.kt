package com.jobik.shkiper.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.modifiers.bounceClick
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
        backgroundColor = if (isActive) CustomTheme.colors.active else Color.Transparent,
        contentColor = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary
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
        backgroundColor = if (isActive) CustomTheme.colors.active else Color.Transparent,
        contentColor = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = if (isActive) CustomTheme.colors.textOnActive else CustomTheme.colors.textSecondary
        )
    }
}