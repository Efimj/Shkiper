package com.jobik.shkiper.ui.components.layouts

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

/**
 * @param icon Can be ImageVector or Int for drawable resource.
 */
@Composable
fun ScreenStub(
    modifier: Modifier = Modifier,
    icon: Any,
    @StringRes title: Int,
    @StringRes description: Int? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StubIcon(icon = icon)
        Spacer(modifier = Modifier.height(10.dp))
        StubTitle(title = title)
        description?.let {
            Spacer(Modifier.height(5.dp))
            StubDescription(description = it)
        }
    }
}

@Composable
private fun StubIcon(icon: Any) {
    AnimatedContent(icon) {
        Icon(
            painter = when (it) {
                is ImageVector -> rememberVectorPainter(it)
                is Int -> painterResource(it)
                else -> throw IllegalArgumentException("Unsupported icon type")
            },
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
private fun StubTitle(@StringRes title: Int) {
    AnimatedContent(title) {
        Text(
            text = stringResource(it),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.text,
        )
    }
}

@Composable
private fun StubDescription(@StringRes description: Int) {
    AnimatedContent(description) {
        Text(
            text = stringResource(it),
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.textSecondary,
        )
    }
}
