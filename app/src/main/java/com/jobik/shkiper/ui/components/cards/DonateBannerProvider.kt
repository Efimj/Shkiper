package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jobik.shkiper.R
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.navigateToSecondary
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.SupportTheDeveloperBannerUtil

@Composable
fun DonateBannerProvider(navController: NavController) {
    val context = LocalContext.current
    val isBannerNeeded =
        rememberSaveable { mutableStateOf(SupportTheDeveloperBannerUtil.isBannerNeeded(context)) }

    AnimatedVisibility(visible = isBannerNeeded.value) {
        Card(
            modifier = Modifier
                .bounceClick(0.95f)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    SupportTheDeveloperBannerUtil.updateLastShowingDate(context)
                    navController.navigateToSecondary(Route.Purchases.route)
                    isBannerNeeded.value = false
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
                    modifier = Modifier.weight(1f)
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
                            SupportTheDeveloperBannerUtil.updateLastShowingDate(context)
                            isBannerNeeded.value = false
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
}