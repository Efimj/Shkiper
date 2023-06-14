package com.example.notepadapp.ui.components.modals.CreateReminderDialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class ReminderDialogViewModel @Inject constructor(
    private val repository: ReminderMongoRepository,
) : ViewModel() {
    private var isInitialized by mutableStateOf(false)
    private var noteIds = mutableStateOf(emptyList<ObjectId>())
    var reminders by mutableStateOf(emptyList<Reminder>())
    val date = mutableStateOf(LocalDate.now())
    val time = mutableStateOf(LocalTime.now())
    val repeatMode = mutableStateOf(RepeatMode.NONE)

    fun initialize(ids: List<ObjectId>) {
        if (isInitialized) return else isInitialized = true
        noteIds.value = ids
        getReminders(ids)
    }

    private fun getReminders(ids: List<ObjectId>) {
        viewModelScope.launch {
            reminders = repository.getRemindersForNotes(noteIds.value)
            if (ids.size == 1 && reminders.isNotEmpty()) {
                date.value = reminders.first().date
                time.value = reminders.first().time
                repeatMode.value = reminders.first().repeat
            } else {
                date.value = LocalDate.now()
                time.value = LocalTime.now()
                repeatMode.value = RepeatMode.NONE
            }
        }
    }
}