package com.android.notepad.ui.components.cards

import com.android.notepad.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.notepad.ui.theme.CustomAppTheme
import com.android.notepad.util.SnackbarVisualsCustom

@Composable
fun SnackbarCard(snackbarData: SnackbarVisualsCustom) {
    Box(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            elevation = 6.dp,
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, CustomAppTheme.colors.mainBackground),
            backgroundColor = CustomAppTheme.colors.secondaryBackground,
            contentColor = CustomAppTheme.colors.text,
        ) {
            Row(
                Modifier.padding(horizontal = 13.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(5.dp))
                if (snackbarData.icon != null) {
                    Icon(
                        tint = CustomAppTheme.colors.textSecondary,
                        imageVector = snackbarData.icon,
                        contentDescription = stringResource(R.string.Event),
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    text = snackbarData.message,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.text,
                )
            }
        }
    }
}