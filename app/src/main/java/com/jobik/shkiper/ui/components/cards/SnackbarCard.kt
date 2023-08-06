package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RoundedButton
import com.jobik.shkiper.ui.theme.CustomAppTheme
import com.jobik.shkiper.util.SnackbarVisualsCustom

@OptIn(ExperimentalFoundationApi::class)
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
                Modifier.padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(17.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.basicMarquee().weight(1f)
                ) {
                    if (snackbarData.icon != null) {
                        Icon(
                            tint = CustomAppTheme.colors.textSecondary,
                            imageVector = snackbarData.icon,
                            contentDescription = stringResource(R.string.Event),
                            modifier = Modifier.padding(vertical = 13.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                    Text(
                        text = snackbarData.message,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        color = CustomAppTheme.colors.text,
                        modifier = Modifier.padding(vertical = 13.dp)
                    )
                    Spacer(Modifier.width(13.dp))
                }
                if (snackbarData.actionLabel != null) {
                    RoundedButton(
                        text = snackbarData.actionLabel,
                        onClick = { snackbarData.action?.let { it() } },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = CustomAppTheme.colors.active,
                            disabledBackgroundColor = Color.Transparent
                        ),
                        border = null,
                        textColor = Color.White
                    )
                }
            }
        }
    }
}