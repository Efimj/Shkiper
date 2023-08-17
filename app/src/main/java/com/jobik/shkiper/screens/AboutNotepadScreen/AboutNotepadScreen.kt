package com.jobik.shkiper.screens.AboutNotepadScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.theme.CustomAppTheme
import java.time.LocalDate


@Composable
fun AboutNotepadScreen() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val statisticsService = StatisticsService(context)

        statisticsService.appStatistics.apply {
            truthSeeker.increment()
        }
        statisticsService.saveStatistics()
    }

    ScreenWrapper(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(top = 85.dp, bottom = 30.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.padding(bottom = 10.dp), horizontalArrangement = Arrangement.Center) {
            Image(
                modifier = Modifier.widthIn(max = 120.dp),
                painter = painterResource(id = R.drawable.ic_app),
                contentDescription = stringResource(R.string.Image),
                contentScale = ContentScale.Fit
            )
        }
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.app_name),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6.copy(fontSize = 25.sp, fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text = stringResource(R.string.AboutAppDescription),
                color = CustomAppTheme.colors.textSecondary,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
        Column() {

        }
    }
}