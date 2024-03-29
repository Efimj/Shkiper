package com.jobik.shkiper.screens.OnboardingScreen

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.jobik.shkiper.SharedPreferencesKeys.OnboardingFinishedData
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val pagerState = rememberPagerState() {
        OnBoardingPage.PageList.Count
    }
    Box {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
            pageSpacing = 10.dp,
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(0.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PagerScreen(OnBoardingPage.PageList.PageList[it])
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ScreenFooter(navController, pagerState, scrollState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenFooter(navController: NavController, pagerState: PagerState, scrollState: ScrollState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val buttonNextContentColor: Color by animateColorAsState(
        targetValue = if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) CustomTheme.colors.textOnActive else CustomTheme.colors.text,
        label = "buttonNextContentColor"
    )

    val buttonNextBackgroundColor: Color by animateColorAsState(
        targetValue = if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) CustomTheme.colors.active else CustomTheme.colors.secondaryBackground,
        label = "buttonNextBackgroundColor"
    )

    Row(
        Modifier
            .widthIn(max = 500.dp)
            .height(70.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage > 0,
            enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
            exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
        )
        {
            Row {
                Button(
                    modifier = Modifier.fillMaxHeight(),
                    shape = CustomTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CustomTheme.colors.text,
                        containerColor = CustomTheme.colors.secondaryBackground
                    ),
                    border = null,
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.Back),
                        tint = CustomTheme.colors.text
                    )
                }
                Spacer(modifier = Modifier.padding(end = 10.dp))
            }
        }
        Button(
            modifier = Modifier
                .testTag("button_next")
                .fillMaxHeight()
                .fillMaxWidth(),
            shape = CustomTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                contentColor = buttonNextContentColor,
                containerColor = buttonNextBackgroundColor
            ),
            border = null,
            elevation = null,
            contentPadding = PaddingValues(horizontal = 15.dp),
            onClick = {
                if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) {
                    onFinished(context, navController)
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        ) {
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
                    Text(
                        text = stringResource(R.string.Finish),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.SemiBold,
                        color = buttonNextContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = stringResource(R.string.Next),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.SemiBold,
                        color = buttonNextContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
        sharedPreferences.edit().putString(SharedPreferencesKeys.OnboardingPageFinishedData, OnboardingFinishedData)
            .apply()
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
        Box(modifier = Modifier.height(440.dp), contentAlignment = Alignment.Center) {
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
                .padding(top = 10.dp),
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
