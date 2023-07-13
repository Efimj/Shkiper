package com.android.notepad.screens.StatisticsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.R
import com.android.notepad.services.statistics_service.StatisticsStorage
import com.android.notepad.ui.theme.CustomAppTheme

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
                Row (modifier = Modifier.padding(top = 45.dp, bottom = 10.dp),horizontalArrangement = Arrangement.Center){
                    Text(
                        stringResource(R.string.Statistics),
                        color = CustomAppTheme.colors.text,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
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