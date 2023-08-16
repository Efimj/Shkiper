package com.jobik.shkiper.screens.StatisticsScreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.services.statistics_service.StatisticsItem
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.cards.StatisticsCard
import com.jobik.shkiper.ui.components.modals.StatisticsInformationDialog
import com.jobik.shkiper.ui.theme.CustomAppTheme
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper

@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val statistics = remember { StatisticsService(context).appStatistics.getStatisticsPreviews() }
    val openedStatistics = remember { mutableStateOf<StatisticsItem?>(null) }

    ScreenWrapper{
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(5.dp)
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Row(
                    modifier = Modifier.padding(top = 80.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.Statistics),
                        color = CustomAppTheme.colors.text,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }
            items(statistics) { property ->
                Box(Modifier.padding(5.dp)) {
                    StatisticsCard(property) { openedStatistics.value = property }
                }
            }
        }
    }
    if (openedStatistics.value != null) {
        StatisticsInformationDialog(openedStatistics.value!!) { openedStatistics.value = null }
    }
}