package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun DonateBanner(
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier
            .widthIn(max = 350.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable { multipleEventsCutter.processEvent { onClick() } },
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = null,
        backgroundColor = AppTheme.colors.container,
        contentColor = AppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.padding(8.dp).padding(start = 8.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.DonateBannerHeader),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                    color = AppTheme.colors.text,
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.Close),
                        tint = AppTheme.colors.textSecondary,
                    )
                }
            }
            Text(
                text = stringResource(R.string.DonateBannerBody),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(fontSize = 16.sp),
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}