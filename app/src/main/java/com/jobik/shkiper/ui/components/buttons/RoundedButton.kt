package com.jobik.shkiper.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.circularRotation
import com.jobik.shkiper.ui.theme.CustomAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = RoundedCornerShape(15.dp),
    border: BorderStroke? = BorderStroke(1.dp, CustomAppTheme.colors.stroke),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        disabledBackgroundColor = Color.Transparent
    ),
    horizontalPaddings: Dp = 0.dp,
    textColor: Color = CustomAppTheme.colors.text,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    iconTint: Color = CustomAppTheme.colors.text,
    content: @Composable (() -> Unit)? = null,
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Button(
        modifier = modifier,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        shape = shape,
        colors = colors,
        border = border,
        elevation = null,
        enabled = enabled
    ) {
        Spacer(Modifier.width(horizontalPaddings))
        if (content != null) {
            content()
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = if (loading) Modifier.size(20.dp).circularRotation() else Modifier.size(20.dp)
                )
                if (text != null)
                    Spacer(Modifier.width(5.dp))
            }
            if (text != null) {
                Text(
                    text = text,
                    //letterSpacing = 0.sp,
                    color = textColor,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(Modifier.width(horizontalPaddings))
    }
}
