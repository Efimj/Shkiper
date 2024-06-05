package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.services.statistics.StatisticsItem
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsCard(statistic: StatisticsItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.container, contentColor = AppTheme.colors.text)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.weight(.6f).padding(top = 12.dp, bottom = 8.dp),
                painter = painterResource(id = statistic.image),
                contentDescription = stringResource(R.string.StatisticsImage),
                contentScale = ContentScale.FillHeight
            )
            Column(
                modifier = Modifier.weight(.4f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    statistic.getStringValue(),
                    color = AppTheme.colors.text,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.basicMarquee().padding(horizontal = 12.dp),
                    maxLines = 1,
                )
                Text(
                    stringResource(statistic.title),
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().basicMarquee().padding(horizontal = 12.dp),
                    maxLines = 1,
                )
            }
        }
    }
}

