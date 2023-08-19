package com.jobik.shkiper.screens.AboutNotepadScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.theme.CustomAppTheme


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
                modifier = Modifier.widthIn(max = 100.dp),
                painter = painterResource(id = R.drawable.ic_app),
                contentDescription = stringResource(R.string.Image),
                contentScale = ContentScale.Fit
            )
        }
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.app_name),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6.copy(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
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
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Text(
                text = stringResource(R.string.Contact),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(5.dp))
            UserCard()
        }
    }
}

@Composable
private fun UserCard() {
    val context = LocalContext.current
    val email = stringResource(R.string.jobik_link)
    val emailHeader = stringResource(R.string.DevMailHeader)

    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.photo_my_favorite_cat),
                modifier = Modifier.widthIn(max = 60.dp).clip(RoundedCornerShape(15.dp)),
                contentDescription = stringResource(R.string.DevMailHeader),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.Efim),
                    color = CustomAppTheme.colors.text,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = stringResource(R.string.EfimDescription),
                    color = CustomAppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    IntentHelper().sendMailIntent(context, listOf(email), emailHeader)
                },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_gmail),
                    contentDescription = stringResource(R.string.Image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}