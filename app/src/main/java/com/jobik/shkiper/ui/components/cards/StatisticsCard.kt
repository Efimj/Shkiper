package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.jobik.shkiper.services.statistics_service.StatisticsItem
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsCard(statistic: StatisticsItem, onClick: () -> Unit) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier
            .height(130.dp)
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = { multipleEventsCutter.processEvent { onClick() } }),
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        //border = BorderStroke(1.dp, CustomTheme.colors.stroke),
        backgroundColor = CustomTheme.colors.secondaryBackground,
        contentColor = CustomTheme.colors.text,
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
                    color = CustomTheme.colors.text,
                    style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.basicMarquee().padding(horizontal = 12.dp),
                    maxLines = 1,
                )
                Text(
                    stringResource(statistic.title),
                    color = CustomTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().basicMarquee().padding(horizontal = 12.dp),
                    maxLines = 1,
                )
            }
        }
    }
}

