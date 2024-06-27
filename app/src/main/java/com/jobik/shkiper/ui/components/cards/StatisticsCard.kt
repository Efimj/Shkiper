package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.services.statistics.StatisticsItem
import com.jobik.shkiper.services.statistics.StatisticsType
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun StatisticsCard(statistic: StatisticsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.medium)
            .background(AppTheme.colors.container)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = stringResource(id = statistic.title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(id = statistic.description),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
            )
        }

        val height by remember { mutableStateOf(60.dp) }

        Box(
            modifier = Modifier
                .heightIn(height)
                .widthIn(height)
                .clip(AppTheme.shapes.small)
                .background(AppTheme.colors.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            when (statistic.type) {
                StatisticsType.Boolean -> {
                    Icon(
                        modifier = Modifier.padding(10.dp).size(30.dp),
                        imageVector = if (statistic.getStringValue()
                                .toBoolean()
                        ) Icons.Outlined.CheckCircle else Icons.Outlined.Close,
                        contentDescription = null,
                        tint = AppTheme.colors.onSecondaryContainer
                    )
                }

                StatisticsType.Long -> {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = statistic.getStringValue(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.onSecondaryContainer,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                else -> {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = statistic.getStringValue(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.onSecondaryContainer,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}