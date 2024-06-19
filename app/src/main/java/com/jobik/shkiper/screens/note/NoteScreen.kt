package com.jobik.shkiper.screens.note

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteScreen(
    onBack: () -> Unit,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    BackHandler(true) {
        onBack()
    }

    NoteScreenContent(
        noteViewModel = noteViewModel,
        onBack = onBack
    )
    NoteScreenRemindersContent(noteViewModel)
    LeaveScreenIfNeeded(noteViewModel = noteViewModel, onBack = onBack)

    DisposableEffect(Unit) {
        onDispose {
            noteViewModel.deleteNoteIfEmpty()
        }
    }
}

@Composable
private fun LeaveScreenIfNeeded(
    noteViewModel: NoteViewModel,
    onBack: () -> Unit,
) {
    LaunchedEffect(noteViewModel.screenState.value.isGoBack) {
        noteViewModel.refreshLinks()
        if (noteViewModel.screenState.value.isGoBack) onBack()
    }
}