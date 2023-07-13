package com.android.notepad.screens.StatisticsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.notepad.services.statistics_service.StatisticsStorage

@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val statistics = remember { StatisticsStorage().getStatistics(context) }

    Column(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Row {
                    Text("Statistics", color = Color.White)
                    Text(statistics.openAppCount.toString(), color = Color.White)
                }
            }
            for (s in 0..33)
                item {
                    Text(s.toString(), color = Color.White)
                }
        }
    }
}

@Composable
fun StatisticsItem() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

    }
}