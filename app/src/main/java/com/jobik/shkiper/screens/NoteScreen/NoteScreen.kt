package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    NoteScreenContent(noteViewModel, navController)
    NoteScreenRemindersContent(noteViewModel)
    LeaveScreenIfNeeded(noteViewModel, navController)
}

@Composable
private fun LeaveScreenIfNeeded(
    noteViewModel: NoteViewModel,
    navController: NavController
) {
    LaunchedEffect(noteViewModel.screenState.value.isGoBack) {
        noteViewModel.runFetchingLinksMetaData()
        if (noteViewModel.screenState.value.isGoBack) navController.popBackStack()
    }
}