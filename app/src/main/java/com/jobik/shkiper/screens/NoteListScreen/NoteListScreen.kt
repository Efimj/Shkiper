package com.jobik.shkiper.screens.NoteListScreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun NoteListScreen(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    NoteListScreenContent(navController, notesViewModel)
}