package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun ActionDialog(
    title: String,
    icon: ImageVector? = null,
    confirmText: String,
    onConfirm: () -> Unit,
    goBackText: String,
    onGoBack: () -> Unit,
) {
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(CustomTheme.colors.mainBackground)
                .padding(horizontal = 25.dp).padding(top = 25.dp, bottom = 10.dp)
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
                        tint = CustomTheme.colors.textSecondary,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.width(7.dp))
                }
                Text(
                    color = CustomTheme.colors.text,
                    text = title,
                    style = MaterialTheme.typography.h6,
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
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = CustomTheme.colors.mainBackground,
                            disabledBackgroundColor = Color.Transparent
                        ),
                        border = null,
                        textColor = CustomTheme.colors.text,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.width(7.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        text = confirmText,
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = CustomTheme.colors.active,
                            disabledBackgroundColor = Color.Transparent
                        ),
                        border = null,
                        textColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}