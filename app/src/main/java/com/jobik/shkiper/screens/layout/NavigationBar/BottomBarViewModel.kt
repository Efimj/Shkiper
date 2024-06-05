package com.jobik.shkiper.screens.layout.NavigationBar

import androidx.lifecycle.ViewModel
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
) : ViewModel() {
    suspend fun createNewNote(): ObjectId {
        val newNote = Note()
        noteRepository.insertNote(newNote)
        return newNote._id
    }
}