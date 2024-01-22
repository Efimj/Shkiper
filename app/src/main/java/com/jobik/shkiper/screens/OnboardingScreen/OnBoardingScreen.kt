package com.jobik.shkiper.screens.OnboardingScreen

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val pagerState = rememberPagerState() {
        OnBoardingPage.PageList.Count
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
            pageSpacing = 10.dp,
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 20.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PagerScreen(OnBoardingPage.PageList.PageList[it])
            }
        }
        ScreenFooter(navController, pagerState, scrollState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenFooter(navController: NavController, pagerState: PagerState, scrollState: ScrollState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val actionsBackgroundColor: Color by animateColorAsState(
        targetValue = if (!scrollState.canScrollForward) CustomTheme.colors.mainBackground else CustomTheme.colors.secondaryBackground,
        label = "actionsBackgroundColor"
    )

    Row(
        Modifier
            .fillMaxWidth()
            .background(actionsBackgroundColor)
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.weight(1f)) {
            AnimatedVisibility(
                visible = pagerState.currentPage > 0,
                enter = slideInHorizontally {
                    it
                } + expandHorizontally(
                    // Expand from the left.
                    expandFrom = Alignment.Start
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
            ) {
                CustomButton(
                    text = stringResource(R.string.Back), onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    style = ButtonStyle.Text
                )
            }
        }
        Row(
            Modifier
                .align(Alignment.CenterVertically)
                .weight(1f), horizontalArrangement = Arrangement.Center
        ) {
            repeat(OnBoardingPage.PageList.Count) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) CustomTheme.colors.text else CustomTheme.colors.textSecondary
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(7.dp)
                )
            }
        }
        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            AnimatedContent(
                targetState = pagerState.currentPage == OnBoardingPage.PageList.Count - 1,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { height -> height } + fadeIn()).togetherWith(slideOutHorizontally { height -> -height } + fadeOut())
                    } else {
                        (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(slideOutHorizontally { height -> height } + fadeOut())
                    }.using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            ) { value ->
                if (value) {
                    CustomButton(
                        modifier = Modifier.testTag("button_next"),
                        text = stringResource(R.string.Finish),
                        onClick = { onFinished(context, navController) },
                        style = ButtonStyle.Filled,
                    )
                } else {
                    CustomButton(
                        modifier = Modifier.testTag("button_next"),
                        text = stringResource(R.string.Next),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        style = ButtonStyle.Text,
                    )
                }
            }
        }
    }
}

fun onFinished(context: Context, navController: NavController) {
    try {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(SharedPreferencesKeys.IsOnboardingPageFinished, true).apply()
    } catch (e: Exception) {
        Log.i("onboarding - onFinished", e.toString())
    }
    navController.navigate(AppScreens.NoteList.route) {
        popUpTo(AppScreens.Onboarding.route) {
            inclusive = true
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(modifier = Modifier.height(480.dp), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxHeight(),
                painter = painterResource(id = onBoardingPage.image),
                contentScale = ContentScale.FillHeight,
                contentDescription = stringResource(R.string.PagerImage)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = stringResource(onBoardingPage.title),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = CustomTheme.colors.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
            text = stringResource(onBoardingPage.description),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            color = CustomTheme.colors.textSecondary,
            minLines = 4,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}
