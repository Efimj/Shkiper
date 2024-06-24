package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.verticalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.util.ThemeUtil
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

private fun Modifier.onboardingScreenPaddings() = this
    .fillMaxSize()
    .verticalWindowInsetsPadding()
    .padding(bottom = 90.dp)
    .padding(horizontal = 40.dp)

@Composable
fun FirstOnboardingScreen() {
    fun rain(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 15f,
                damping = 0.9f,
                delay = 200,
                angle = Angle.TOP,
                spread = Spread.ROUND,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 150, TimeUnit.DAYS).perSecond(25),
                timeToLive = 2700,
                position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
            )
        )
    }

    val isDarkTheme = ThemeUtil.isDarkMode.value == true
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

    val fullText = stringResource(id = R.string.onb_title_1)
    val targetWord = stringResource(id = R.string.app_name)

    val annotatedString = buildAnnotatedString {
        val startIndex = fullText.indexOf(targetWord)
        if (startIndex != -1) {
            append(fullText.substring(0, startIndex))
            withStyle(style = SpanStyle(color = color)) {
                append(targetWord)
            }
            append(fullText.substring(startIndex + targetWord.length))
        } else {
            append(fullText)
        }
    }

    Box {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = rain(),
        )
        Column(
            modifier = Modifier.onboardingScreenPaddings(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Crossfade(
                    targetState = index,
                    animationSpec = tween(animationDelay),
                    label = "crossfade"
                ) { index ->
                    Image(
                        modifier = Modifier.size(200.dp),
                        imageVector = ImageVector.vectorResource(id = icons[index]),
                        contentDescription = "icon",
                        contentScale = ContentScale.FillHeight
                    )
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = annotatedString,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                color = AppTheme.colors.primary,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.onb_description_1),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.colors.onSecondaryContainer,
            )
        }
    }
}

@Composable
fun SecondOnboardingScreen() {
    Column(
        modifier = Modifier.onboardingScreenPaddings(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

    }
}