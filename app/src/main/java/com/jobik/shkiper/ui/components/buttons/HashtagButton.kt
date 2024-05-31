package com.jobik.shkiper.ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun HashtagButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean = false,
    onClick: (String) -> Unit
) {
    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "buttonContentColor"
    )

    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.primary else AppTheme.colors.container,
        label = "buttonBackgroundColor"
    )

    Card(
        modifier = modifier,
        onClick = { onClick(text) },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = buttonBackgroundColor,
            contentColor = buttonContentColor
        ),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp),
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            color = buttonContentColor,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis
        )
    }
}