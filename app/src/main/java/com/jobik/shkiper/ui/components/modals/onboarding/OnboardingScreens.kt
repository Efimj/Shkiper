package com.jobik.shkiper.ui.components.modals.onboarding

import android.health.connect.datatypes.units.Velocity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Widgets
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.ui.unit.IntOffset
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
import kotlin.random.Random

private fun Modifier.onboardingScreenPaddings() = this
    .fillMaxSize()
    .verticalWindowInsetsPadding()
    .padding(bottom = 90.dp, top = 40.dp)
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
private fun FeatureCard(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(AppTheme.shapes.medium)
                .background(AppTheme.colors.secondaryContainer)
                .padding(7.dp)
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onSecondaryContainer
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun SecondOnboardingScreen() {
    val features1 = remember {
        listOf(
            Triple(
                Icons.Outlined.Widgets,
                R.string.feature_title_1,
                R.string.feature_description_1,
            ),
            Triple(
                Icons.Outlined.Notifications,
                R.string.feature_title_2,
                R.string.feature_description_2,
            ),
            Triple(
                Icons.Outlined.TextFields,
                R.string.feature_title_3,
                R.string.feature_description_3,
            ),
        )
    }

    val features2 = remember {
        listOf(
            Triple(
                Icons.Outlined.Notifications,
                R.string.feature_title_2,
                R.string.feature_description_2,
            ),
            Triple(
                Icons.Outlined.Widgets,
                R.string.feature_title_1,
                R.string.feature_description_1,
            ),
            Triple(
                Icons.Outlined.TextFields,
                R.string.feature_title_3,
                R.string.feature_description_3,
            ),
        )
    }

    val features3 = remember {
        listOf(
            Triple(
                Icons.Outlined.TextFields,
                R.string.feature_title_3,
                R.string.feature_description_3,
            ),
            Triple(
                Icons.Outlined.Widgets,
                R.string.feature_title_1,
                R.string.feature_description_1,
            ),
            Triple(
                Icons.Outlined.Notifications,
                R.string.feature_title_2,
                R.string.feature_description_2,
            ),
        )
    }

    var currentFeatures by remember { mutableStateOf(Triple(0, 0, 0)) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            currentFeatures =
                currentFeatures.copy(first = (currentFeatures.first + 1) % features1.size)
            delay(500)
            currentFeatures =
                currentFeatures.copy(second = (currentFeatures.second + 1) % features2.size)
            delay(500)
            currentFeatures =
                currentFeatures.copy(third = (currentFeatures.third + 1) % features3.size)
            delay(2000)
        }
    }

    Column(
        modifier = Modifier.onboardingScreenPaddings(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 40.dp),
            text = stringResource(id = R.string.onb_title_2),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.onSecondaryContainer,
            overflow = TextOverflow.Ellipsis,
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FeaturesPresentation(features1[currentFeatures.first])
            FeaturesPresentation(features2[currentFeatures.second])
            FeaturesPresentation(features3[currentFeatures.third])
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