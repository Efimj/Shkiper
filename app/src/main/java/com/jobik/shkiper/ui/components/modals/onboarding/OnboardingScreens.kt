package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.jobik.shkiper.ui.helpers.verticalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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
                emitter = Emitter(duration = 150, TimeUnit.DAYS).perSecond(20),
                timeToLive = 3000,
                position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
            )
        )
    }

    val icons = remember {
        listOf(
            R.drawable.ic_classic_launcher,
            R.drawable.ic_classic_white_launcher,
            R.drawable.ic_love_launcher,
            R.drawable.ic_rocket_launcher,
            R.drawable.ic_night_launcher,
            R.drawable.ic_money_launcher,
        ).shuffled()
    }

    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            index = (index + 1) % icons.size
            println(index)
        }
    }

    Box {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = rain(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalWindowInsetsPadding()
                .padding(bottom = 90.dp)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Crossfade(
                    targetState = index,
                    animationSpec = tween(500),
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
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.onb_title_1),
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
                text = "New notepad application for your all notes and more",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.colors.onSecondaryContainer,
            )
        }
    }
}