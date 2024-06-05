package com.jobik.shkiper.screens.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

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
        noteViewModel.refreshLinks()
        if (noteViewModel.screenState.value.isGoBack) navController.popBackStack()
    }
}