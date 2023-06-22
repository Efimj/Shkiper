package com.example.notepadapp.screens.NoteScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.navigation.ARGUMENT_NOTE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteMongoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var isTopAppBarHover by mutableStateOf(false)
    var isBottomAppBarHover by mutableStateOf(false)

    var noteId by mutableStateOf(ObjectId(savedStateHandle[ARGUMENT_NOTE_ID] ?: ""))
    val note = repository.getNote(noteId)
    var noteHeader by mutableStateOf(note?.header ?: "")
    var noteBody by mutableStateOf(note?.body ?: "")
    var noteIsPinned by mutableStateOf(note?.isPinned ?: false)
    var noteUpdatedDate by mutableStateOf(note?.updateDate ?: Date())

    init {
        viewModelScope.launch { }
    }

    fun updateNoteHeader(text: String) {
        noteHeader = text
        noteUpdatedDate = Date()
        updateNote {
            it.header = this@NoteViewModel.noteHeader
            it.updateDate = this@NoteViewModel.noteUpdatedDate
        }
    }

    fun updateNoteBody(text: String) {
        noteBody = text
        noteUpdatedDate = Date()
        updateNote {
            it.body = this@NoteViewModel.noteBody
            it.updateDate = this@NoteViewModel.noteUpdatedDate
        }
    }

    fun switchNotePinnedMode() {
        noteIsPinned = !noteIsPinned
        updateNote {
            it.isPinned = this@NoteViewModel.noteIsPinned
        }
    }

    private fun updateNote(updateParams: (Note) -> Unit) {
        if (this@NoteViewModel.note == null) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(this@NoteViewModel.note._id, updateParams)
        }
    }

    fun deleteNoteIfEmpty() {
        viewModelScope.launch {
            if (noteHeader.isEmpty() && noteBody.isEmpty())
                repository.deleteNote(noteId)
        }
    }

    fun saveChanges() {
        updateNote {
            it.header = this@NoteViewModel.noteHeader
            it.body = this@NoteViewModel.noteBody
            it.updateDate = this@NoteViewModel.noteUpdatedDate
            it.isPinned = this@NoteViewModel.noteIsPinned
        }
    }
}