package com.example.notepadapp.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.ui.theme.CustomAppTheme

@Composable
fun CreateNoteCard(
    text: String = "",
    isActive: Boolean = true,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .height(110.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                indication = if (isActive) LocalIndication.current else null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                if (isActive) onClick()
            },
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, if (isActive) CustomAppTheme.colors.stroke else Color.Transparent),
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = CustomAppTheme.colors.textSecondary,
                modifier = Modifier.size(45.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                color = CustomAppTheme.colors.textSecondary,
            )
        }
    }
}
