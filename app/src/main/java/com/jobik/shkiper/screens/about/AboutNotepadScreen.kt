package com.jobik.shkiper.screens.about

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.jobik.shkiper.BuildConfig
import com.jobik.shkiper.R
import com.jobik.shkiper.services.statistics.StatisticsService
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.util.ContextUtils.isDarkModeEnabled
import com.jobik.shkiper.util.settings.NightMode
import com.jobik.shkiper.util.settings.SettingsManager
import kotlinx.coroutines.delay

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .allWindowInsetsPadding()
            .padding(top = 85.dp, bottom = 30.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        Spacer(modifier = Modifier.height(30.dp))
        Column {

        }
    }
}

@Composable
private fun Header() {
    val context = LocalContext.current

    val isDarkTheme = when (SettingsManager.settings.nightMode) {
        NightMode.Light -> false
        NightMode.Dark -> true
        else -> isDarkModeEnabled(context)
    }

    val iconsWithStrings = remember {
        listOf(
            Pair(
                R.drawable.ic_classic_launcher,
                if (isDarkTheme) CustomThemeStyle.MaterialDynamicColors.dark.primary else CustomThemeStyle.MaterialDynamicColors.light.primary
            ),
            Pair(
                R.drawable.ic_classic_white_launcher,
                if (isDarkTheme) CustomThemeStyle.MaterialDynamicColors.dark.text else CustomThemeStyle.MaterialDynamicColors.light.text
            ),
            Pair(
                R.drawable.ic_love_launcher,
                if (isDarkTheme) CustomThemeStyle.PastelRed.dark.primary else CustomThemeStyle.PastelRed.light.primary
            ),
            Pair(
                R.drawable.ic_rocket_launcher,
                if (isDarkTheme) CustomThemeStyle.PastelOrange.dark.primary else CustomThemeStyle.PastelOrange.light.primary
            ),
            Pair(
                R.drawable.ic_night_launcher,
                if (isDarkTheme) CustomThemeStyle.PastelOrange.dark.textSecondary else CustomThemeStyle.PastelOrange.light.textSecondary
            ),
            Pair(
                R.drawable.ic_money_launcher,
                if (isDarkTheme) CustomThemeStyle.PastelGreen.dark.primary else CustomThemeStyle.PastelGreen.light.primary
            )
        ).shuffled()
    }

    val icons = iconsWithStrings.map { it.first }
    val colors = iconsWithStrings.map { it.second }

    val animationDelay = 500

    var index by remember { mutableIntStateOf(0) }
    val color by animateColorAsState(
        targetValue = colors[index],
        animationSpec = tween(animationDelay)
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            index = (index + 1) % icons.size
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 30.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Crossfade(
            targetState = index,
            animationSpec = tween(animationDelay),
            label = "crossfade"
        ) { index ->
            Image(
                modifier = Modifier.size(160.dp),
                imageVector = ImageVector.vectorResource(id = icons[index]),
                contentDescription = "icon",
            )
        }
        Column {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Column {
                Text(
                    text = stringResource(R.string.made_by_efim),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.textSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = BuildConfig.VERSION_NAME,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.textSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}