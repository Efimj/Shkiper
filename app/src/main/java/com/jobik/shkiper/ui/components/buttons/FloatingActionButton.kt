package com.jobik.shkiper.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun FloatingActionButton(
    icon: ImageVector = Icons.Outlined.Add,
    isActive: Boolean = true,
    onClick: () -> Unit = {},
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier.testTag("create_note_button").bounceClick().clip(RoundedCornerShape(15.dp)).clickable(
            indication = if (isActive) LocalIndication.current else null,
            interactionSource = remember { MutableInteractionSource() } // This is mandatory
        ) {
            if (isActive) {
                multipleEventsCutter.processEvent { onClick() }
            }
        },
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(if (isActive) 2.dp else 0.dp, CustomTheme.colors.active),
        backgroundColor = CustomTheme.colors.secondaryBackground,
        contentColor = CustomTheme.colors.text,
    ) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.CreateNote),
            tint = CustomTheme.colors.textSecondary,
            modifier = Modifier.size(53.dp).padding(6.dp)
        )
    }
}