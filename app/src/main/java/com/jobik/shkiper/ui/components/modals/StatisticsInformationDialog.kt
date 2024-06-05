package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.services.statistics.StatisticsItem
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun StatisticsInformationDialog(
    statistics: StatisticsItem,
    onGoBack: () -> Unit,
) {
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(AppTheme.colors.container)
                .padding(vertical = 20.dp).height(340.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.weight(.65f).padding(vertical = 15.dp),
                    painter = painterResource(id = statistics.image),
                    contentDescription = stringResource(R.string.StatisticsImage),
                    contentScale = ContentScale.FillHeight
                )
                Column(
                    modifier = Modifier.weight(.45f).padding(horizontal = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        statistics.getStringValue(),
                        color = AppTheme.colors.text,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        stringResource(statistics.title),
                        color = AppTheme.colors.text,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        stringResource(statistics.description),
                        color = AppTheme.colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}