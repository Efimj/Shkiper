package com.jobik.shkiper.ui.components.layouts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun SettingsGroup(
    header: String? = null,
    accent: Boolean = false,
    columnScope: @Composable ColumnScope.() -> Unit
) {
    val accentColor =
        animateColorAsState(
            targetValue = if (accent) AppTheme.colors.primary else Color.Transparent,
            label = "accentColor"
        )

    val borderWidth =
        animateDpAsState(
            targetValue = if (accent) 2.dp else 0.dp,
            label = "borderWidth"
        )

    Column(
        modifier = Modifier
            .clip(AppTheme.shapes.large)
            .background(AppTheme.colors.container)
            .border(
                width = borderWidth.value,
                shape = AppTheme.shapes.large,
                color = accentColor.value
            )
            .padding(vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!header.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
                color = AppTheme.colors.primary,
                text = header,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        columnScope()
    }
}
