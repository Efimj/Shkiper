package com.android.notepad.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.R
import com.android.notepad.helpers.NumberHelper
import com.android.notepad.services.statistics_service.StatisticsItem
import com.android.notepad.ui.modifiers.bounceClick
import com.android.notepad.ui.theme.CustomAppTheme


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
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, CustomAppTheme.colors.stroke),
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.height(60.dp).padding(vertical = 5.dp),
                painter = painterResource(id = statistic.image),
                contentDescription = stringResource(R.string.StatisticsImage),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    statistic.getStringValue(),
                    color = CustomAppTheme.colors.text,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                )
                Text(
                    stringResource(statistic.title),
                    color = CustomAppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                )
            }
        }
    }
}

