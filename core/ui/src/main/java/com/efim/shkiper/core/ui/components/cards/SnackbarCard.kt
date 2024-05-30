package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.SnackbarVisualsCustom

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SnackbarCard(snackbarData: SnackbarVisualsCustom) {
    Box(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(
                contentColor = AppTheme.colors.onSecondaryContainer,
                containerColor = AppTheme.colors.secondaryContainer
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .basicMarquee()
                        .padding(start = 17.dp)
                        .padding(horizontal = 13.dp)
                        .weight(1f)
                ) {
                    if (snackbarData.icon != null) {
                        Icon(
                            tint = AppTheme.colors.onSecondaryContainer,
                            imageVector = snackbarData.icon,
                            contentDescription = stringResource(R.string.Event),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                    Text(
                        text = snackbarData.message,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colors.onSecondaryContainer,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Spacer(Modifier.width(13.dp))
                }
                if (snackbarData.actionLabel != null) {
                    CustomButton(
                        text = snackbarData.actionLabel,
                        onClick = { snackbarData.action?.let { it() } },
                        style = ButtonStyle.Filled
                    )
                }
            }
        }
    }
}