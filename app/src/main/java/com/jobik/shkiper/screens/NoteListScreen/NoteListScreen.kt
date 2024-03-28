package com.jobik.shkiper.screens.NoteListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.screens.AppLayout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.CalendarViewModel
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.ScreenCalendarContent
import com.jobik.shkiper.screens.NoteListScreen.NoteListScreenContent.NoteListScreenContent
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun NoteListScreen(navController: NavController) {
    val selectedPageNumber = rememberSaveable { mutableIntStateOf(1) }
    ReturnUserToMainContent(currentPage = selectedPageNumber)

    AnimatedContent(
        modifier = Modifier.fillMaxSize(),
        targetState = selectedPageNumber.intValue,
//        transitionSpec = {
//            if (initialState == 1) {
//                ScreenTransition().secondaryScreenEnterTransition()
//                    .togetherWith(ExitTransition.None)
//            } else {
//                EnterTransition.None.togetherWith(ScreenTransition().secondaryScreenExitTransition())
//            }
//        },
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