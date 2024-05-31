package com.jobik.shkiper.ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun MainMenuButton(text: String, icon: ImageVector? = null, isActive: Boolean = false, onClick: () -> Unit = { }) {
    val menuButtonModifier = Modifier
        .fillMaxWidth()
        .height(45.dp)

    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (isActive) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "buttonContentColor"
    )

    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (isActive) AppTheme.colors.primary else AppTheme.colors.container,
        label = "buttonBackgroundColor"
    )

    Button(
        modifier = menuButtonModifier,
        shape = AppTheme.shapes.small,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            contentColor = buttonContentColor,
            containerColor = buttonBackgroundColor
        ),
        border = null,
        elevation = null,
        contentPadding = PaddingValues(horizontal = 15.dp),
        onClick = onClick
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = buttonContentColor,
                )
                Spacer(Modifier.width(5.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = buttonContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
