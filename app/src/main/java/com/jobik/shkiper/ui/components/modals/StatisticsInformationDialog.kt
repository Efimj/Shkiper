package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.jobik.shkiper.services.statistics_service.StatisticsItem
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun StatisticsInformationDialog(
    statistics: StatisticsItem,
    onGoBack: () -> Unit,
) {
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(CustomTheme.colors.secondaryBackground)
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
                        color = CustomTheme.colors.text,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        stringResource(statistics.title),
                        color = CustomTheme.colors.text,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        stringResource(statistics.description),
                        color = CustomTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}