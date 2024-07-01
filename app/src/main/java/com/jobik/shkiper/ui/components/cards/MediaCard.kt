package com.jobik.shkiper.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun MediaCard(
    isHighlight: Boolean = false,
    @DrawableRes image: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val backgroundColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.secondaryContainer else AppTheme.colors.container,
        tween(500)
    )

    val iconColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.onSecondaryContainer,
        tween(500)
    )

    val titleColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.text,
        tween(500)
    )

    val descriptionColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.textSecondary else AppTheme.colors.textSecondary,
        tween(500)
    )

    val scale by animateFloatAsState(if (isHighlight) 1.05f else 1f, tween(500))

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .bounceClick()
            .clip(AppTheme.shapes.large)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(iconColor)
        )
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = descriptionColor,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
