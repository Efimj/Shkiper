package com.jobik.shkiper.ui.components.layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.theme.CustomTheme

data class TopAppBarItem(
    val isActive: Boolean = false,
    val icon: ImageVector,
    @StringRes
    val iconDescription: Int,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit
)

@Composable
fun CustomTopAppBar(
    modifier: Modifier,
    elevation: Dp = 0.dp,
    text: String = "",
    backgroundColor: Color = CustomTheme.colors.secondaryBackground,
    contentColor: Color = CustomTheme.colors.textSecondary,
    navigation: TopAppBarItem,
    items: List<TopAppBarItem>
) {
    TopAppBar(
        elevation = elevation,
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        modifier = modifier,
        title = {
            Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            Text(
                text,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                color = CustomTheme.colors.textSecondary,
                maxLines = 1,
            )
        },
        navigationIcon = {
            Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = navigation.onClick,
                modifier = navigation.modifier.size(40.dp).clip(CircleShape).padding(0.dp),
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
                    modifier = item.modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.iconDescription),
                        tint = if (item.isActive) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            }
        })
}