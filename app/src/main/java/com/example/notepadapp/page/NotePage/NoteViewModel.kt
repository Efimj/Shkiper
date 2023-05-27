package com.example.notepadapp.page.NotePage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.NoteMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.navigation.ARGUMENT_NOTE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
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
    var noteUpdatedDate by mutableStateOf(note?.updateDate ?: Date())

    init {
        viewModelScope.launch {

        }
    }

    fun updateNoteHeader(text: String) {
        noteHeader = text
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note = Note().apply {
                if(note == null) return@launch
                _id = this@NoteViewModel.note._id
                header = this@NoteViewModel.noteHeader
                body = this@NoteViewModel.note.body
                updateDate = this@NoteViewModel.note.updateDate
                creationDate = this@NoteViewModel.note.creationDate
                deletionDate = this@NoteViewModel.note.deletionDate
                hashtags = this@NoteViewModel.note.hashtags
                isPinned = this@NoteViewModel.note.isPinned
                position = this@NoteViewModel.note.position
            })
        }
    }

    fun updateNoteBody(text: String) {
        noteBody = text
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note = Note().apply {
                if(note == null) return@launch
                _id = this@NoteViewModel.note._id
                header = this@NoteViewModel.noteHeader
                body = this@NoteViewModel.noteBody
                updateDate = this@NoteViewModel.note.updateDate
                creationDate = this@NoteViewModel.note.creationDate
                deletionDate = this@NoteViewModel.note.deletionDate
                hashtags = this@NoteViewModel.note.hashtags
                isPinned = this@NoteViewModel.note.isPinned
                position = this@NoteViewModel.note.position
            })
        }
    }
}