package com.jobik.shkiper.screens.AboutNotepadScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.BuildConfig
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.cards.UserCard
import com.jobik.shkiper.ui.components.cards.UserCardLink
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.theme.CustomTheme


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
        Row(modifier = Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(.65f)
                    .clip(CustomTheme.shapes.large)
                    .background(CustomTheme.colors.secondaryBackground)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = CustomTheme.colors.active,
                    style = MaterialTheme.typography.h6.copy(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = stringResource(R.string.AboutAppDescription),
                    color = CustomTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(1f).fillMaxWidth(1.33f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(.65f).fillMaxWidth()
                        .clip(CustomTheme.shapes.large)
                        .background(CustomTheme.colors.secondaryBackground)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.widthIn(max = 100.dp),
                        painter = painterResource(id = R.drawable.ic_app),
                        contentDescription = stringResource(R.string.Image),
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(1.35f).fillMaxWidth()
                        .clip(CustomTheme.shapes.large)
                        .background(CustomTheme.colors.secondaryBackground)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "V ${BuildConfig.VERSION_NAME}",
                        color = CustomTheme.colors.active,
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Text(
                text = stringResource(R.string.Contact),
                color = CustomTheme.colors.textSecondary,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val email = stringResource(R.string.jobik_link)
            val emailHeader = stringResource(R.string.DevMailHeader)
            UserCard(
                photo = R.drawable.photo_my_favorite_cat_2,
                name = stringResource(R.string.Efim),
                description = stringResource(R.string.EfimDescription),
                onClick = { IntentHelper().sendMailIntent(context, listOf(email), emailHeader) },
                links = listOf(
                    UserCardLink(
                        image = R.drawable.ic_gmail,
                        description = stringResource(R.string.Image),
                    ) { IntentHelper().sendMailIntent(context, listOf(email), emailHeader) }
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.Icons),
                color = CustomTheme.colors.textSecondary,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                val rakibHassanRahimLink = stringResource(R.string.RakibHassanRahimLink)
                UserCard(
                    photo = R.drawable.rakib_hassan_rahim,
                    onClick = { IntentHelper().openBrowserIntent(context, rakibHassanRahimLink) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                val freepikLink = stringResource(R.string.FreepikLink)
                UserCard(
                    photo = R.drawable.freepik,
                    onClick = { IntentHelper().openBrowserIntent(context, freepikLink) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                val stickersLink = stringResource(R.string.StickersLink)
                UserCard(
                    photo = R.drawable.stickers_pack,
                    onClick = { IntentHelper().openBrowserIntent(context, stickersLink) }
                )
            }
        }
    }
}