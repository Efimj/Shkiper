package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.components.layouts.VerticalIndicator
import com.jobik.shkiper.ui.components.modals.FullscreenPopup
import com.jobik.shkiper.ui.theme.AppTheme

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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { }) {

                        }
                    }
                    BottomNavigation(pagerState = pagerState, onFinish)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BottomNavigation(pagerState: PagerState, onFinish: () -> Unit) {
    Box(
        modifier = Modifier.align(Alignment.BottomCenter),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

        }
    }
}

@Composable
private fun BoxScope.IndicatorContent(pagerState: PagerState) {
    Box(modifier = Modifier.align(Alignment.TopStart), contentAlignment = Alignment.CenterStart) {
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