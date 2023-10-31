package com.jobik.shkiper.screens.OnboardingScreen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.CustomButton
import kotlinx.coroutines.launch
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.theme.CustomTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
    val pagerState = rememberPagerState() {
        OnBoardingPage.PageList.Count
    }

    Column(modifier = Modifier.fillMaxSize().padding(vertical = 30.dp)) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            pageSpacing = 0.dp,
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(0.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
        ) {
            PagerScreen(OnBoardingPage.PageList.PageList[it])
        }
        ScreenFooter(navController, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenFooter(navController: NavController, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.weight(1f)) {
            if (pagerState.currentPage > 0)
                CustomButton(
                    text = stringResource(R.string.Back), onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    style = ButtonStyle.Text
                )
        }
        Row(
            Modifier.align(Alignment.CenterVertically).weight(1f), horizontalArrangement = Arrangement.Center
        ) {
            repeat(OnBoardingPage.PageList.Count) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) CustomTheme.colors.text else CustomTheme.colors.textSecondary
                Box(
                    modifier = Modifier.padding(2.dp).clip(CircleShape).background(color).size(7.dp)
                )
            }
        }
        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            CustomButton(
                modifier = Modifier.testTag("button_next"),
                text = if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) stringResource(R.string.Finish) else stringResource(
                    R.string.Next
                ),
                onClick = {
                    if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) {
                        onFinished(context, navController)
                    } else coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                style = if (pagerState.currentPage == OnBoardingPage.PageList.Count - 1) ButtonStyle.Filled else ButtonStyle.Text
            )
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
        modifier = Modifier.fillMaxSize(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = stringResource(R.string.PagerImage)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(onBoardingPage.title),
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = CustomTheme.colors.text
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = stringResource(onBoardingPage.description),
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = CustomTheme.colors.textSecondary
        )
    }
}
