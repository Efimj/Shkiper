package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.FeatureCard
import com.jobik.shkiper.ui.components.cards.Features
import com.jobik.shkiper.ui.helpers.splitIntoTriple
import com.jobik.shkiper.ui.helpers.verticalWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.bounceClick
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

fun Onboardingscreens(): List<@Composable () -> Unit> = listOf(
    { FirstOnboardingScreen() },
    { SecondOnboardingScreen() },
    { ThirdOnboardingScreen() }
)

private fun Modifier.onboardingScreenPaddings() = this
    .fillMaxSize()
    .verticalWindowInsetsPadding()
    .padding(bottom = 90.dp, top = 40.dp)
    .padding(horizontal = 40.dp)

@Composable
private fun FirstOnboardingScreen() {
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
                color = AppTheme.colors.onSecondaryContainer,
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
private fun FeaturesPresentation(
    feature: Triple<ImageVector, Int, Int>
) {
    AnimatedContent(
        targetState = feature,
        transitionSpec = {
            (fadeIn(animationSpec = tween(500, 500)) + scaleIn(animationSpec = tween(500, 500)))
                .togetherWith(fadeOut(animationSpec = tween(500)))
        },
    ) { feature ->
        FeatureCard(
            icon = feature.first,
            title = stringResource(id = feature.second),
            description = stringResource(id = feature.third)
        )
    }
}

@Composable
private fun SecondOnboardingScreen() {
    val features = remember { splitIntoTriple(Features.shuffled()) }
    var currentFeatures by remember { mutableStateOf(Triple(0, 0, 0)) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            currentFeatures =
                currentFeatures.copy(first = (currentFeatures.first + 1) % features.first.size)
            delay(500)
            currentFeatures =
                currentFeatures.copy(second = (currentFeatures.second + 1) % features.second.size)
            delay(500)
            currentFeatures =
                currentFeatures.copy(third = (currentFeatures.third + 1) % features.third.size)
            delay(2000)
        }
    }

    Column(
        modifier = Modifier.onboardingScreenPaddings(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 30.dp),
            text = stringResource(id = R.string.onb_title_2),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.onSecondaryContainer,
            overflow = TextOverflow.Ellipsis,
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FeaturesPresentation(features.first[currentFeatures.first])
            FeaturesPresentation(features.second[currentFeatures.second])
            FeaturesPresentation(features.third[currentFeatures.third])
        }
    }
}

@Composable
private fun MediaCard(
    isHighlight: Boolean = false,
    @DrawableRes image: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val backgroundColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.secondaryContainer else AppTheme.colors.container,
        tween(500)
    )

    val iconColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.onSecondaryContainer,
        tween(500)
    )

    val titleColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.text,
        tween(500)
    )

    val descriptionColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.textSecondary else AppTheme.colors.textSecondary,
        tween(500)
    )

    val scale by animateFloatAsState(if (isHighlight) 1.05f else 1f, tween(500))

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .bounceClick()
            .clip(AppTheme.shapes.large)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(iconColor)
        )
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = descriptionColor,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ThirdOnboardingScreen() {
    val count = 3
    var selected by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            selected = (selected + 1) % count
            delay(2000)
        }
    }

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = Modifier.onboardingScreenPaddings(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val firstTitleColor by
        animateColorAsState(
            targetValue = if (selected < 2) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary,
            tween(500)
        )

        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = stringResource(id = R.string.onb_title_3),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = firstTitleColor,
            overflow = TextOverflow.Ellipsis,
        )
        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            MediaCard(
                isHighlight = selected == 0,
                image = R.drawable.ic_github,
                title = stringResource(R.string.source_code),
                description = stringResource(R.string.source_code_description),
            ) {
                uriHandler.openUri(context.getString(R.string.shkiper_github_link))
            }
            MediaCard(
                isHighlight = selected == 1,
                image = R.drawable.ic_telegram,
                title = stringResource(R.string.telegram_chat),
                description = stringResource(R.string.telegram_chat_description)
            ) {
                uriHandler.openUri(context.getString(R.string.telegram_link))
            }
        }
        val secondTitleColor by
        animateColorAsState(
            targetValue = if (selected == 2) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary,
            tween(500)
        )

        Text(
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
            text = "Bonus",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = secondTitleColor,
            overflow = TextOverflow.Ellipsis,
        )
        MediaCard(
            isHighlight = selected == 2,
            image = R.drawable.ic_game_of_life,
            title = stringResource(id = R.string.game_of_life),
            description = stringResource(R.string.game_of_life_description)
        ) {
            uriHandler.openUri(context.getString(R.string.game_of_life_link))
        }
    }
}