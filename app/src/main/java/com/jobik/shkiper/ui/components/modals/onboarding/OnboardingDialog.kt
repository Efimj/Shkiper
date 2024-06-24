package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.layouts.VerticalIndicator
import com.jobik.shkiper.ui.components.modals.FullscreenPopup
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.verticalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingDialog(isVisible: Boolean, onFinish: () -> Unit) {
    val pagerState = rememberPagerState { 3 }
    val animationState = remember { MutableTransitionState(false) }
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (visible.value.not()) {
            visible.value = true
        }
        animationState.targetState = isVisible
    }

    LaunchedEffect(animationState.isIdle) {
        if (animationState.isIdle) {
            visible.value = isVisible
        }
    }

    if (visible.value) {
        FullscreenPopup(
            onDismiss = {}
        ) {
            BackHandler {
                onFinish()
            }
            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visibleState = animationState,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalWindowInsetsPadding()
                        .background(AppTheme.colors.background)
                ) {
                    IndicatorContent(pagerState = pagerState)
                    VerticalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        userScrollEnabled = true,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        reverseLayout = false,
                        contentPadding = PaddingValues(0.dp),
                        pageSize = PageSize.Fill,
                    ) {
                        when (it) {
                            0 -> FirstOnboardingScreen()
                        }
                    }
                    BottomNavigation(pagerState = pagerState, onFinish = onFinish)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BottomNavigation(pagerState: PagerState, onFinish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val isFinish = pagerState.currentPage >= pagerState.pageCount - 1

    val (color, background) = listOf(
        animateColorAsState(targetValue = if (isFinish) AppTheme.colors.onPrimary else AppTheme.colors.onSecondaryContainer).value,
        animateColorAsState(targetValue = if (isFinish) AppTheme.colors.primary else AppTheme.colors.secondaryContainer).value
    )

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .bottomWindowInsetsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(modifier = Modifier
                .weight(1f)
                .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = color,
                    containerColor = background
                ),
                onClick = {
                    if (isFinish) {
                        onFinish()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                }) {
                Crossfade(
                    targetState = isFinish,
                    label = "FinishButton"
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        if (it) {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = stringResource(id = R.string.Finish),
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium
                            )
                        } else {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = stringResource(id = R.string.Next),
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.IndicatorContent(pagerState: PagerState) {
    Box(
        modifier = Modifier
            .verticalWindowInsetsPadding()
            .align(Alignment.TopStart),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalIndicator(pagerState)
        }
    }
}