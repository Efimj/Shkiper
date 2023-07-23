package com.android.notepad.screens.StatisticsScreen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.notepad.R
import com.android.notepad.services.statistics_service.StatisticsItem
import com.android.notepad.services.statistics_service.StatisticsService
import com.android.notepad.services.statistics_service.StatisticsStorage
import com.android.notepad.ui.components.cards.StatisticsCard
import com.android.notepad.ui.components.modals.StatisticsInformationDialog
import com.android.notepad.ui.theme.CustomAppTheme
import kotlin.math.log

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val statistics = remember { StatisticsService(context).appStatistics.getStatisticsPreviews() }
    val openedStatistics = remember { mutableStateOf<StatisticsItem?>(null) }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val lazyVerticalGridCellsMode: GridCells = remember {
        if (isPortrait) GridCells.Fixed(3) else GridCells.Adaptive(128.dp)
    }

    Column(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = lazyVerticalGridCellsMode,
            contentPadding = PaddingValues(10.dp)
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Row(
                    modifier = Modifier.padding(top = 75.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.Statistics),
                        color = CustomAppTheme.colors.text,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(statistics) { property ->
                Box(Modifier.padding(7.dp)) {
                    StatisticsCard(property) { openedStatistics.value = property }
                }
            }
        }
    }
    if (openedStatistics.value != null) {
        StatisticsInformationDialog(openedStatistics.value!!) { openedStatistics.value = null }
    }
}