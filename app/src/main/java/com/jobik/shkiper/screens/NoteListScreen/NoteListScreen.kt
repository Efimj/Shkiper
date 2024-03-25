package com.jobik.shkiper.screens.NoteListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.screens.AppLayout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.CalendarViewModel
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.ScreenCalendarContent
import com.jobik.shkiper.screens.NoteListScreen.NoteListScreenContent.NoteListScreenContent
import com.jobik.shkiper.viewmodels.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(navController: NavController) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    ReturnUserToMainContent(pagerState, scope)

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        state = pagerState,
        pageSpacing = 0.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = PaddingValues(0.dp),
        beyondBoundsPageCount = 0,
        pageSize = PageSize.Fill,
    ) {
        if (it == 0) {
            NoteListScreenContent(
                navController = navController,
                viewModel = hiltViewModel<NotesViewModel>(),
                onSlideNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                })
        } else {
            ScreenCalendarContent(
                navController = navController,
                viewModel = hiltViewModel<CalendarViewModel>(),
                onSlideBack = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            )
        }
    }

    LaunchedEffect(pagerState.targetPage) {
        if (pagerState.targetPage > 0) {
            AppNavigationBarState.hideWithLock()
        } else {
            AppNavigationBarState.showWithUnlock()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReturnUserToMainContent(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    BackHandler(enabled = pagerState.currentPage == 1) {
        scope.launch {
            pagerState.animateScrollToPage(0)
        }
    }
}