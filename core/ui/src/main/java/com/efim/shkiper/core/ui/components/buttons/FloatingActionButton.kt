package com.jobik.shkiper.ui.components.buttons

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun FloatingActionButton(
    icon: ImageVector = Icons.Outlined.Add,
    isActive: Boolean = true,
    onClick: () -> Unit = {},
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Surface(
        shape = MaterialTheme.shapes.small, shadowElevation = 1.dp, color = AppTheme.colors.container,
        modifier = Modifier
            .testTag("create_note_button")
            .bounceClick()
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                indication = if (isActive) LocalIndication.current else null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                if (isActive) {
                    multipleEventsCutter.processEvent { onClick() }
                }
            },
        border = BorderStroke(if (isActive) 2.dp else 0.dp, AppTheme.colors.primary),
        contentColor = AppTheme.colors.text,
    ) {
        AnimatedContent(
            targetState = icon,
            transitionSpec = {
                (slideInHorizontally { height -> height } + fadeIn())
                    .togetherWith(slideOutHorizontally { height -> -height } + fadeOut())
                    .using(SizeTransform(clip = false))
            }, label = ""
        ) { newIcon ->
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = newIcon,
                    contentDescription = stringResource(R.string.CreateNote),
                    tint = AppTheme.colors.textSecondary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}