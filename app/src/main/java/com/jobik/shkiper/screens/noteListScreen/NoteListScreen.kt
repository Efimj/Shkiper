package com.jobik.shkiper.screens.noteListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.screens.layout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.screens.noteListScreen.NoteListCalendarContent.CalendarViewModel
import com.jobik.shkiper.screens.noteListScreen.NoteListCalendarContent.ScreenCalendarContent
import com.jobik.shkiper.screens.noteListScreen.NoteListScreenContent.NoteListScreenContent
import com.jobik.shkiper.viewmodels.NotesViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListScreen(
    navController: NavController,
) {
    val selectedPageNumber = rememberSaveable { mutableIntStateOf(1) }
    ReturnUserToMainContent(currentPage = selectedPageNumber)

    AnimatedContent(
        modifier = Modifier.fillMaxSize(),
        targetState = selectedPageNumber.intValue,
        transitionSpec = {
            val duration = 300

            if (targetState > initialState) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(duration),
                ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = duration),
                    targetOffset = { -150 }
                )
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(duration),
                    initialOffset = { -150 }
                ).togetherWith(
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(duration),
                    )
                )
            }.apply {
                targetContentZIndex = targetState.toFloat()
            }
        },
        label = "MainPageAnimatedContent"
    ) { number ->
        if (number == 1) {
            NoteListScreenContent(
                navController = navController,
                viewModel = hiltViewModel<NotesViewModel>(),
                onSlideNext = {
                    selectedPageNumber.intValue = 2
                })
        } else {
            ScreenCalendarContent(
                navController = navController,
                viewModel = hiltViewModel<CalendarViewModel>(),
                onSlideBack = {
                    selectedPageNumber.intValue = 1
                }
            )
        }
    }

    LaunchedEffect(selectedPageNumber.intValue) {
        if (selectedPageNumber.intValue > 1) {
            AppNavigationBarState.hideWithLock()
        } else {
            AppNavigationBarState.showWithUnlock()
        }
    }
}

@Composable
private fun ReturnUserToMainContent(
    currentPage: MutableIntState
) {
    BackHandler(enabled = currentPage.intValue == 2) {
        currentPage.intValue = 1
    }
}