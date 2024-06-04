package com.jobik.shkiper.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

data class SettingsItemColors(
    val contentColor: Color? = null,
    val leadingIconColor: Color? = null,
    val containerColor: Color? = null,
)

@Composable
fun SettingsItem(
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier.heightIn(min = 50.dp),
    icon: ImageVector? = null,
    title: String,
    isEnabled: Boolean = true,
    isActive: Boolean = false,
    description: String? = null,
    onClick: (() -> Unit) = {},
    colors: SettingsItemColors = SettingsItemColors(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
    action: (@Composable () -> Unit)? = null
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = colors.containerColor
            ?: if (isActive) AppTheme.colors.primary else Color.Transparent,
        label = "backgroundColor",
    )

    val foregroundColor: Color by animateColorAsState(
        targetValue = colors.contentColor
            ?: if (isActive) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "foregroundColor"
    )

    val foregroundSecondaryColor: Color by animateColorAsState(
        targetValue = colors.leadingIconColor
            ?: if (isActive) AppTheme.colors.onPrimary else AppTheme.colors.textSecondary,
        label = "foregroundSecondaryColor"
    )

    Card(
        onClick = onClick, enabled = isEnabled, colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = foregroundColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = foregroundColor
        ), shape = AppTheme.shapes.none, border = null
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = icon == null, enter = scaleIn(), exit = scaleOut()) {
                Spacer(Modifier.padding(start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr)))
            }
            AnimatedContent(targetState = icon, label = "AnimatedContent - icon") {
                if (it != null) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = contentPadding.calculateLeftPadding(
                                LayoutDirection.Ltr
                            )
                        )
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .fillMaxSize(),
                            tint = foregroundSecondaryColor
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                AnimatedContent(targetState = title, label = "AnimatedContent - title") {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = foregroundColor
                    )
                }
                AnimatedVisibility(
                    visible = description != null,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Spacer(modifier = Modifier.height(3.dp))
                }
                AnimatedContent(
                    targetState = description,
                    label = "AnimatedContent - description"
                ) {
                    if (it != null)
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            color = foregroundSecondaryColor
                        )
                }
            }
            AnimatedContent(
                targetState = action,
                label = "AnimatedContent - action"
            ) {
                if (it != null)
                    Row(
                        modifier = Modifier.padding(
                            horizontal = contentPadding.calculateRightPadding(
                                LayoutDirection.Ltr
                            )
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        it()
                    }
            }
        }
    }
}