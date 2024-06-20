package com.jobik.shkiper.screens.layout.NavigationBar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

data class BottomBarState(
    val createdNoteId: ObjectId? = null
)

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
) : ViewModel() {
    private val _screenState = mutableStateOf(BottomBarState())
    val screenState: State<BottomBarState> = _screenState

    suspend fun createNewNote(): ObjectId {
        val newNote = Note()
        noteRepository.insertNote(newNote)
        _screenState.value = _screenState.value.copy(createdNoteId = newNote._id)
        return newNote._id
    }
}