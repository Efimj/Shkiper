package com.jobik.shkiper.screens.note

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteScreen(
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    NoteScreenContent(
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope,
        noteViewModel = noteViewModel,
        onBack = onBack
    )
    NoteScreenRemindersContent(noteViewModel)
    LeaveScreenIfNeeded(noteViewModel = noteViewModel, onBack = onBack)
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