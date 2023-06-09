package com.example.notepadapp.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme

@Composable
fun CreateNoteButton(
    isActive: Boolean = true,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(15.dp)).clickable(
            indication = if (isActive) LocalIndication.current else null,
            interactionSource = remember { MutableInteractionSource() } // This is mandatory
        ) { if (isActive) onClick() },
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(if (isActive) 1.dp else 0.dp, CustomAppTheme.colors.stroke),
        backgroundColor = CustomAppTheme.colors.mainBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Create note",
            tint = CustomAppTheme.colors.textSecondary,
            modifier = Modifier.size(53.dp).padding(6.dp)
        )
    }
}