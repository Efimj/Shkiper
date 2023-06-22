package com.example.notepadapp.screens.NoteListScreen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.R
import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.notification_service.NotificationData
import com.example.notepadapp.notification_service.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.*
import javax.inject.Inject

//data class NoteCard(
//    var title: String,
//    val description: String,
//    var isSelected: Boolean = false
//)
//
//val NoteCards = listOf(
//    NoteCard(
//        "Покупки на неделю",
//        "список продуктов, которые нужно купить на следующую неделю для ежедневных приготовлений пищи.",
//    ),
//    NoteCard(
//        "Список задач на сегодня",
//        "перечень задач, которые необходимо выполнить в течение дня для достижения целей.",
//    ),
//    NoteCard("Идеи для отпуска", "записи о потенциальных местах, которые можно посетить в следующем отпуске."),
//    NoteCard("Список любимых книг", "перечень книг, которые прочитал и рекомендую другим для чтения."),
//    NoteCard(
//        "План тренировок",
//        "программа тренировок на неделю, которую нужно выполнить для достижения целей в фитнесе.",
//    ),
//    NoteCard(
//        "Планирование бюджета",
//        "записи о расходах и доходах, которые нужно учесть при планировании бюджета на месяц.",
//    ),
//    NoteCard("Идеи для новых проектов", "идеи о новых проектах, которые можно начать в ближайшее время."),
//    NoteCard("Список контактов", "перечень контактов, которые могут быть полезны в работе или личной жизни."),
//    NoteCard("План поездки", "записи о дате, месте и бюджете планируемой поездки."),
//    NoteCard("Список цитат", "перечень любимых цитат, которые вдохновляют и мотивируют на достижение целей."),
//    NoteCard("Список целей на год", "перечень основных целей, которые нужно достичь в течение года."),
//    NoteCard(
//        "Список дел на выходные",
//        "перечень дел, которые можно выполнить на выходных для отдыха и улучшения настроения."
//    ),
//    NoteCard("Список интересных фильмов", "перечень фильмов, которые рекомендуются к просмотру в свободное время."),
//    NoteCard("План обучения новому навыку", "план обучения и практики нового навыка для его освоения."),
//    NoteCard("Список желаемых покупок", "перечень вещей, которые хотелось бы приобрести в ближайшее время."),
//    NoteCard("План обновления гардероба", "план покупок и обновления гардероба на сезон."),
//    NoteCard("Список любимых рецептов", "перечень любимых рецептов, которые можно приготовить для себя и близких."),
//    NoteCard("План обновления дома", "план обновления и ремонта дома на ближайшее время."),
//    NoteCard("Список целей на месяц", "перечень целей, которые нужно достичь в течение месяца."),
//    NoteCard("Идеи для подарков", "записи о потенциальных подарках для близких и друзей на различные праздники."),
//    NoteCard("sss", "wddww")
//)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    private val application: Application
) : ViewModel() {
    /*******************
     * Notes region
     *******************/
    val pinnedNotes = mutableStateOf(emptyList<Note>())
    val notes = mutableStateOf(emptyList<Note>())
    var lastCreatedNoteId by mutableStateOf("")
    var searchText by mutableStateOf("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getNotes()
            getReminders()
        }
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes(true).collect() {
                pinnedNotes.value = it
            }
        }
        viewModelScope.launch {
            noteRepository.getNotes().collect() {
                notes.value = it
            }
        }
    }

    private val _selectedNoteCardIndices = mutableStateOf(setOf<ObjectId>())
    val selectedNotes: State<Set<ObjectId>> = _selectedNoteCardIndices

    fun clearSelectedNote() {
        _selectedNoteCardIndices.value = setOf()
    }

    fun changeSearchText(newString: String) {
        searchText = newString
        viewModelScope.launch(Dispatchers.IO) {
            if (newString.isEmpty()) {
                getNotes()
            } else {
                getNotesByText(searchText)
            }
        }
    }

    private suspend fun getNotesByText(newString: String) {
        pinnedNotes.value = emptyList()
        noteRepository.filterNotesByContains(newString).collect {
            notes.value = it
        }
    }

    fun toggleSelectedNoteCard(index: ObjectId) {
        _selectedNoteCardIndices.value =
            if (_selectedNoteCardIndices.value.contains(index)) _selectedNoteCardIndices.value.minus(index)
            else _selectedNoteCardIndices.value.plus(index)
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            noteRepository.deleteNote(selectedNotes.value.toList())
            clearSelectedNote()
        }
    }

    fun pinSelectedNotes() {
        viewModelScope.launch {
            val pinMode: Boolean
            var unpinnedNote = notes.value.find { note ->
                selectedNotes.value.any {
                    it == note._id
                } && !note.isPinned
            }
            if (unpinnedNote == null) {
                unpinnedNote = pinnedNotes.value.find { note ->
                    selectedNotes.value.any {
                        it == note._id
                    } && !note.isPinned
                }
            }
            pinMode = unpinnedNote != null
            noteRepository.updateNote(selectedNotes.value.toList()) { updatedNote ->
                updatedNote.isPinned = pinMode
            }
            clearSelectedNote()
        }
    }

    fun createNewNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note()
            noteRepository.insertNote(newNote)
            lastCreatedNoteId = newNote._id.toHexString()
        }
    }

    /*******************
     * Reminder region
     *******************/

    private val _reminders = mutableStateOf(emptyList<Reminder>())
    val reminders: State<List<Reminder>> = _reminders

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getAllReminders().collect() {
                _reminders.value = it
            }
        }
    }

    private val _isCreateReminderDialogShow = mutableStateOf(false)
    val isCreateReminderDialogShow: State<Boolean> = _isCreateReminderDialogShow

    fun switchReminderDialogShow() {
        _isCreateReminderDialogShow.value = !_isCreateReminderDialogShow.value
    }

    fun getReminder(noteId: ObjectId):Reminder? {
        return reminderRepository.getReminderForNote(noteId)
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                reminderRepository.updateOrCreateReminderForNotes(selectedNotes.value.toList()) { updatedReminder ->
                    updatedReminder.date = date
                    updatedReminder.time = time
                    updatedReminder.repeat = repeatMode
                }
                val notes = noteRepository.getNotes(selectedNotes.value.toList())
                for (note in notes) {
                    val localDateTime = LocalDateTime.of(date, time)
                    val notificationData = NotificationData(
                        note._id.toHexString(),
                        note._id.timestamp,
                        note.header,
                        note.body,
                        R.drawable.ic_notification,
                        repeatMode,
                        note._id.timestamp,
                        localDateTime.toInstant(OffsetDateTime.now().offset).toEpochMilli()
                    )
                    val notificationScheduler = NotificationScheduler(application.applicationContext)
                    notificationScheduler.createNotificationChannel(NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL)
                    notificationScheduler.scheduleNotification(notificationData)
                }
            }
            switchReminderDialogShow()
        }
    }

    fun deleteSelectedReminder() {
        if (selectedNotes.value.isEmpty()) return
        viewModelScope.launch {
            val reminder = reminderRepository.getReminderForNote(selectedNotes.value.toList().first()) ?: return@launch
            reminderRepository.deleteReminder(reminder._id)
            val notificationScheduler = NotificationScheduler(application.applicationContext)
            notificationScheduler.deleteNotification(reminder.noteId.timestamp)
        }
        switchReminderDialogShow()
    }
}
