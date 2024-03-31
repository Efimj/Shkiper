package com.jobik.shkiper.ui.components.layouts

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.topWindowInsetsPadding
import com.jobik.shkiper.ui.theme.CustomTheme

data class TopAppBarItem(
    val isActive: Boolean = false,
    val icon: ImageVector,
    @StringRes
    val iconDescription: Int,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    text: String = "",
    counter: Int? = null,
    backgroundColor: Color = CustomTheme.colors.secondaryBackground,
    contentColor: Color = CustomTheme.colors.textSecondary,
    navigation: TopAppBarItem,
    items: List<TopAppBarItem>
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
    ) {
        TopAppBar(
            modifier = modifier
                .background(backgroundColor)
                .horizontalWindowInsetsPadding()
                .topWindowInsetsPadding(),
            windowInsets = WindowInsets.ime,
            colors = TopAppBarDefaults.topAppBarColors(
                actionIconContentColor = contentColor,
                titleContentColor = contentColor,
                navigationIconContentColor = contentColor,
                containerColor = backgroundColor,
            ),
            title = {
                if (text.isNotBlank())
                    Text(
                        text = text,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                if (text.isNotBlank() && counter != null)
                    Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                if (counter != null)
                    Counter(
                        count = counter,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                        color = contentColor
                    )
            },
            navigationIcon = {
                IconButton(
                    onClick = navigation.onClick,
                    modifier = navigation.modifier
                ) {
                    Icon(
                        imageVector = navigation.icon,
                        contentDescription = stringResource(navigation.iconDescription),
                        tint = if (navigation.isActive) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                    )
                }
            },
            actions = {
                for (item in items) {
                    IconButton(
                        onClick = item.onClick,
                        modifier = item.modifier
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.iconDescription),
                            tint = if (item.isActive) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                        )
                    }
                }
            })
    }
}