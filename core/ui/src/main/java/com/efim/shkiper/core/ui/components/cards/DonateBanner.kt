package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun DonateBanner(
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier
            .widthIn(max = 350.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { multipleEventsCutter.processEvent { onClick() } },
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
                    .padding(8.dp)
                    .padding(start = 8.dp, bottom = 4.dp)
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
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .padding(0.dp),
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