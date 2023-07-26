package com.android.notepad.ui.components.layouts

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.ui.theme.CustomAppTheme

data class TopAppBarItem(
    val isActive: Boolean = false,
    val icon: ImageVector,
    @StringRes
    val iconDescription: Int,
    val onClick: () -> Unit,

    )

@Composable
fun CustomTopAppBar(
    modifier: Modifier,
    elevation: Dp,
    text: String,
    navigation: TopAppBarItem,
    items: List<TopAppBarItem>
) {
    TopAppBar(
        elevation = elevation,
        contentColor = CustomAppTheme.colors.textSecondary,
        backgroundColor = CustomAppTheme.colors.mainBackground,
        modifier = modifier,
        title = {
            Text(
                text,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                color = CustomAppTheme.colors.textSecondary,
                maxLines = 1,
            )
        },
        navigationIcon = {
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = navigation.onClick,
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = navigation.icon,
                    contentDescription = stringResource(navigation.iconDescription),
                    tint = if (navigation.isActive) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                )
            }
        },
        actions = {
            for (item in items) {
                IconButton(
                    onClick = item.onClick,
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.iconDescription),
                        tint = if (navigation.isActive) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            }
        })
}