package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun ActionDialog(
    title: String,
    icon: ImageVector? = null,
    dialogProperties: DialogProperties = DialogProperties(true, dismissOnClickOutside = true),
    confirmText: String,
    onConfirm: () -> Unit,
    goBackText: String,
    onGoBack: () -> Unit,
) {
    Dialog(onGoBack, dialogProperties) {
        Column(
            Modifier
                .clip(AppTheme.shapes.medium)
                .background(AppTheme.colors.container)
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        tint = AppTheme.colors.textSecondary,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.width(15.dp))
                }
                Text(
                    color = AppTheme.colors.text,
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        text = goBackText,
                        onClick = onGoBack,
                        style = ButtonStyle.Text,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        text = confirmText,
                        onClick = onConfirm,
                        style = ButtonStyle.Filled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}