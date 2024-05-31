package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.animateColorAsState
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

@Composable
fun SettingsItem(
    icon: ImageVector? = null,
    title: String,
    isEnabled: Boolean = true,
    isActive: Boolean = false,
    modifier: Modifier = Modifier.heightIn(min = 50.dp),
    description: String? = null,
    onClick: (() -> Unit) = {},
    containerColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
    action: (@Composable () -> Unit)? = null
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (isActive) AppTheme.colors.primary else containerColor ?: Color.Transparent,
        label = "backgroundColor"
    )

    val foregroundColor: Color by animateColorAsState(
        targetValue = if (isActive) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "foregroundColor"
    )

    val foregroundSecondaryColor: Color by animateColorAsState(
        targetValue = if (isActive) AppTheme.colors.onPrimary else AppTheme.colors.textSecondary,
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
                .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon == null)
                Spacer(Modifier.padding(start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr)))
            if (icon !== null)
                Row(modifier = Modifier.padding(horizontal = contentPadding.calculateLeftPadding(LayoutDirection.Ltr))) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .fillMaxSize(),
                        tint = foregroundSecondaryColor
                    )
                }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = foregroundColor
                )
                if (description !== null)
                    Spacer(modifier = Modifier.height(3.dp))
                if (description !== null)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        color = foregroundSecondaryColor
                    )
            }
            Row(
                modifier = Modifier.padding(horizontal = contentPadding.calculateRightPadding(LayoutDirection.Ltr)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (action !== null)
                    action()
            }
        }
    }
}