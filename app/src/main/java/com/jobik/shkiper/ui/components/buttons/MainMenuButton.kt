package com.jobik.shkiper.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun MainMenuButton(text: String, icon: ImageVector? = null, isActive: Boolean = false, onClick: () -> Unit = { }) {
    val menuButtonModifier = Modifier.fillMaxWidth().height(45.dp)

    if (isActive) {
        CustomButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            properties = DefaultButtonProperties(
                border = null,
                buttonColors = ButtonDefaults.buttonColors(backgroundColor = CustomTheme.colors.active),
                textColor = CustomTheme.colors.textOnActive,
                textStyle = MaterialTheme.typography.h6.copy(fontSize = 17.sp).copy(fontWeight = FontWeight.SemiBold),
                iconTint = CustomTheme.colors.textOnActive
            ),
            onClick = onClick,
        )
    } else {
        CustomButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            properties = DefaultButtonProperties(
                buttonColors = ButtonDefaults.buttonColors(backgroundColor = CustomTheme.colors.secondaryBackground),
                border = null,
                textStyle = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
            ),
            onClick = onClick,
        )
    }
}
