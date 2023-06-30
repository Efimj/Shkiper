package com.example.notepadapp.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.ui.theme.CustomAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(15.dp),
    border: BorderStroke? = BorderStroke(1.dp, CustomAppTheme.colors.stroke),
    colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = CustomAppTheme.colors.mainBackground, disabledBackgroundColor = Color.Transparent),
    textColor: Color = CustomAppTheme.colors.text,
    iconTint: Color = CustomAppTheme.colors.text,
    content: @Composable (() -> Unit)? = null,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = colors,
        border = border,
        elevation = null,
        enabled = enabled
    ) {
        if (content != null) {
            content()
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(5.dp))
            }
            if (text != null) {
                Text(
                    text = text,
                    letterSpacing = 0.sp,
                    color = textColor,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.body1,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
