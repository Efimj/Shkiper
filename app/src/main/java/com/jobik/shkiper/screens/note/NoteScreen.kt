package com.jobik.shkiper.screens.note

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.helpers.IntentHelper

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    val context = LocalContext.current
    BackHandler(navController.previousBackStackEntry == null) {
        IntentHelper().startAppActivity(context)
    }

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