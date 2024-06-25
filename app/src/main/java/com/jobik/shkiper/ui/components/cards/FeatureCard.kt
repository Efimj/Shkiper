package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.NewLabel
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.AppTheme

val Features = listOf(
    Triple(
        Icons.Outlined.Widgets,
        R.string.feature_title_1,
        R.string.feature_description_1,
    ),
    Triple(
        Icons.Outlined.Notifications,
        R.string.feature_title_2,
        R.string.feature_description_2,
    ),
    Triple(
        Icons.Outlined.TextFields,
        R.string.feature_title_3,
        R.string.feature_description_3,
    ),
    Triple(
        Icons.Outlined.Event,
        R.string.feature_title_4,
        R.string.feature_description_4,
    ),
    Triple(
        Icons.Outlined.NewLabel,
        R.string.feature_title_5,
        R.string.feature_description_5,
    ),
    Triple(
        Icons.AutoMirrored.Outlined.Reply,
        R.string.feature_title_6,
        R.string.feature_description_6,
    ),
    Triple(
        Icons.Outlined.Visibility,
        R.string.feature_title_7,
        R.string.feature_description_7,
    ),
    Triple(
        Icons.Outlined.DonutLarge,
        R.string.feature_title_8,
        R.string.feature_description_8,
    ),
    Triple(
        Icons.Outlined.FormatPaint,
        R.string.feature_title_9,
        R.string.feature_description_9,
    ),
)

@Composable
fun FeatureCard(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(AppTheme.shapes.medium)
                .background(AppTheme.colors.secondaryContainer)
                .padding(7.dp)
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onSecondaryContainer
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}