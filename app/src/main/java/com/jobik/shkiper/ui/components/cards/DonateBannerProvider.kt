package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun DonateBannerProvider(onClick: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .bounceClick(0.95f)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            },
        shape = MaterialTheme.shapes.medium,
        border = null,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.secondaryContainer,
            contentColor = AppTheme.colors.onSecondaryContainer
        ),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.DonateBannerHeader),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.primary,
                )
                Text(
                    text = stringResource(R.string.DonateBannerBody),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSecondaryContainer,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.Close),
                        tint = AppTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}