package com.example.notepadapp.page.NotePage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.notepadapp.navigation.ARGUMENT_NOTE_ID

class NoteViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val noteId by mutableStateOf(savedStateHandle[ARGUMENT_NOTE_ID] ?: "")
    var isTopAppBarHover by mutableStateOf(false)
    var isBottomAppBarHover by mutableStateOf(false)
}