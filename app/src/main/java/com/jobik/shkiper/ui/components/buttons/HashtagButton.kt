package com.jobik.shkiper.ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.theme.CustomTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HashtagButton(text: String, selected: Boolean = false, onClick: (String) -> Unit) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (selected) CustomTheme.colors.textOnActive else CustomTheme.colors.text,
        label = "buttonContentColor"
    )

    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (selected) CustomTheme.colors.active else CustomTheme.colors.secondaryBackground,
        label = "buttonBackgroundColor"
    )

    Chip(
        onClick = { multipleEventsCutter.processEvent { onClick(text) } },
        shape = RoundedCornerShape(10.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = buttonBackgroundColor,
            contentColor = buttonContentColor
        ),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = buttonContentColor,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis
        )
    }
}