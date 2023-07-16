package com.android.notepad.screens.StatisticsScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.notepad.R
import com.android.notepad.services.statistics_service.StatisticsItem
import com.android.notepad.services.statistics_service.StatisticsStorage
import com.android.notepad.ui.components.cards.StatisticsCard
import com.android.notepad.ui.components.modals.StatisticsInformationDialog
import com.android.notepad.ui.theme.CustomAppTheme

@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val statistics = remember { StatisticsStorage().getStatistics(context) }
    val openedStatistics = remember { mutableStateOf<StatisticsItem?>(null) }

    Column(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
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
            items(statistics.javaClass.declaredFields) { property ->
                property.isAccessible = true
                val value = property.get(statistics)
                if (value is StatisticsItem) {
                    Box(Modifier.padding(7.dp)) {
                        StatisticsCard(value) { openedStatistics.value = value }
                    }
                }
            }
        }
    }
    if (openedStatistics.value != null) {
        StatisticsInformationDialog(openedStatistics.value!!) { openedStatistics.value = null }
    }
}