package com.example.notepadapp.ui.components.modals.CreateReminderDialog

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject


@HiltViewModel
class ReminderDialogViewModel @Inject constructor(
    val id: String,
    private val repository: ReminderMongoRepository,
) : ViewModel() {
    private var isInitialized by mutableStateOf(false)
    private var noteIds by mutableStateOf(emptyList<ObjectId>())
    var reminder by mutableStateOf<Reminder?>(null)

    fun setNoteIds(ids: List<ObjectId>) {
        if (isInitialized) return else isInitialized = true
        noteIds = ids
        if (ids.size == 1) {
            viewModelScope.launch {
                reminder = repository.getReminder(noteIds.first())
            }
        }
    }
}