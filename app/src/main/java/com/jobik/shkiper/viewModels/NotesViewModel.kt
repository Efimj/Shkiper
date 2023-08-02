package com.jobik.shkiper.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.navigation.Argument_Note_Position
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.*
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class NotesScreenState(
    val isNotesInitialized: Boolean = false,
    val notes: List<Note> = emptyList(),
    val lastCreatedNoteId: String = "",
    val searchText: String = "",
    val selectedNotes: Set<ObjectId> = emptySet(),
    val hashtags: Set<String> = emptySet(),
    val currentHashtag: String? = null,
    val reminders: List<Reminder> = emptyList(),
    val isCreateReminderDialogShow: Boolean = false,
    val isDeleteNotesDialogShow: Boolean = false,
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val notePosition: NotePosition
    private val _screenState = mutableStateOf(NotesScreenState())
    val screenState: State<NotesScreenState> = _screenState

    /*******************
     * Notes region
     *******************/

    private var notesFlowJob: Job? = null

    init {
        notePosition = try {
            NotePosition.valueOf(savedStateHandle[Argument_Note_Position] ?: NotePosition.MAIN.name)
        } catch (e: Exception) {
            Log.i("savedStateHandle", "notePositionError", e)
            NotePosition.MAIN
        }
        viewModelScope.launch(Dispatchers.IO) {
            getHashtags()
            getNotes()
            getReminders()
            if (notePosition == NotePosition.DELETE)
                deleteExpiredNotes()
        }
    }

    private fun getNotes() {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesFlow(notePosition).collect() {
                _screenState.value = _screenState.value.copy(notes = it, isNotesInitialized = true)
            }
        }
    }

    fun clearSelectedNote() {
        _screenState.value = screenState.value.copy(
            selectedNotes = setOf(),
            isCreateReminderDialogShow = false,
            isDeleteNotesDialogShow = false
        )
    }

    fun changeSearchText(newString: String) {
        notesFlowJob?.cancel()

        _screenState.value = _screenState.value.copy(searchText = newString)
        notesFlowJob = viewModelScope.launch(Dispatchers.IO) {
            if (newString.isEmpty()) {
                getNotes()
            } else {
                getNotesByText(_screenState.value.searchText)
            }
        }
    }

    private suspend fun getNotesByText(newString: String) {
        _screenState.value = screenState.value.copy(currentHashtag = null)
        noteRepository.filterNotesByContains(newString, notePosition).collect {
            _screenState.value = _screenState.value.copy(notes = it)
        }
    }

    fun toggleSelectedNoteCard(noteId: ObjectId) {
        val selectedNotes = screenState.value.selectedNotes
        _screenState.value = screenState.value.copy(
            selectedNotes = if (selectedNotes.contains(noteId)) selectedNotes.minus(noteId)
            else selectedNotes.plus(noteId)
        )
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            noteRepository.deleteNote(screenState.value.selectedNotes.toList())
            clearSelectedNote()
            showSnackbar(
                message = application.applicationContext.getString(R.string.NotesAreBeenDeleted),
                icon = Icons.Default.DeleteForever
            )
        }
    }

    fun pinSelectedNotes() {
        viewModelScope.launch {
            val pinMode: Boolean
            var unpinnedNote = _screenState.value.notes.filter { it.isPinned }.find { note ->
                screenState.value.selectedNotes.any {
                    it == note._id
                } && !note.isPinned
            }
            if (unpinnedNote == null) {
                unpinnedNote = _screenState.value.notes.filterNot { it.isPinned }.find { note ->
                    screenState.value.selectedNotes.any {
                        it == note._id
                    } && !note.isPinned
                }
            }
            pinMode = unpinnedNote != null
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.isPinned = pinMode
                updatedNote.position = NotePosition.MAIN
            }
            clearSelectedNote()
        }
    }

    fun archiveSelectedNotes() {
        viewModelScope.launch {
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.position = NotePosition.ARCHIVE
                updatedNote.isPinned = false
            }
            clearSelectedNote()
            showSnackbar(
                message = application.applicationContext.getString(R.string.NotesArchived),
                icon = Icons.Default.Archive
            )
        }
    }

    fun unarchiveSelectedNotes() {
        viewModelScope.launch {
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.position = NotePosition.MAIN
            }
            clearSelectedNote()
            showSnackbar(
                message = application.applicationContext.getString(R.string.NotesUnarchived),
                icon = Icons.Default.Unarchive
            )
        }
    }

    fun moveSelectedNotesToBasket() {
        viewModelScope.launch {
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.position = NotePosition.DELETE
                updatedNote.isPinned = false
                updatedNote.deletionDate = LocalDateTime.now()
            }
            clearSelectedNote()
            showSnackbar(
                message = application.applicationContext.getString(R.string.NotesMovedToBasket),
                icon = Icons.Default.DeleteSweep
            )
        }
    }

    fun removeSelectedNotesFromBasket() {
        viewModelScope.launch {
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.position = NotePosition.MAIN
                updatedNote.deletionDate = null
            }
            clearSelectedNote()
            showSnackbar(
                message = application.applicationContext.getString(R.string.NotesRestored),
                icon = Icons.Default.Undo
            )
        }
    }

    fun createNewNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note()
            noteRepository.insertNote(newNote)
            _screenState.value = screenState.value.copy(lastCreatedNoteId = newNote._id.toHexString())
        }
    }

    fun clearLastCreatedNote() {
        _screenState.value = screenState.value.copy(lastCreatedNoteId = "")
    }

    fun clickOnNote(note: Note, currentRoute: String, navController: NavController) {
        if (_screenState.value.selectedNotes.isNotEmpty())
            toggleSelectedNoteCard(note._id)
        else {
            if (currentRoute.substringBefore("/") != AppScreens.Note.route.substringBefore("/")) {
                navController.navigate(AppScreens.Note.noteId(note._id.toHexString()))
            }
        }
    }

    fun switchDeleteDialogShow() {
        _screenState.value =
            _screenState.value.copy(isDeleteNotesDialogShow = !_screenState.value.isDeleteNotesDialogShow)
    }

    fun deleteExpiredNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = noteRepository.getNotes(NotePosition.DELETE)
            val suitableNotes = notes.filter {
                it.position == NotePosition.DELETE && ChronoUnit.DAYS.between(
                    it.deletionDate,
                    LocalDateTime.now()
                ) >= 7
            }
            noteRepository.deleteNote(suitableNotes.map { it._id })
        }
    }

    /*******************
     * Hashtag region
     *******************/

    fun getHashtags() {
        viewModelScope.launch {
            noteRepository.getHashtags(notePosition).collect() {
                _screenState.value = screenState.value.copy(hashtags = it)
            }
        }
    }

    fun setCurrentHashtag(newHashtag: String?) {
        if (newHashtag == screenState.value.currentHashtag) {
            _screenState.value = screenState.value.copy(currentHashtag = null)
            getNotes()
        } else {
            _screenState.value = screenState.value.copy(currentHashtag = newHashtag)
            getNotesByHashtag(screenState.value.currentHashtag ?: "")
        }
    }

    fun getNotesByHashtag(hashtag: String) {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesByHashtag(notePosition, hashtag).collect() {
                _screenState.value = _screenState.value.copy(notes = it)
            }
        }
    }

    /*******************
     * Reminder region
     *******************/

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getReminders().collect() {
                _screenState.value = screenState.value.copy(reminders = it)
            }
        }
    }

    fun switchReminderDialogShow() {
        _screenState.value =
            _screenState.value.copy(isCreateReminderDialogShow = !_screenState.value.isCreateReminderDialogShow)
    }

    fun getReminder(noteId: ObjectId): Reminder? {
        return reminderRepository.getReminderForNote(noteId)
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                reminderRepository.updateOrCreateReminderForNotes(
                    noteRepository.getNotesFlow(screenState.value.selectedNotes.toList()),
                ) { updatedReminder ->
                    updatedReminder.date = date
                    updatedReminder.time = time
                    updatedReminder.repeat = repeatMode
                }
            }
            switchReminderDialogShow()
        }
    }

    fun deleteSelectedReminder() {
        if (screenState.value.selectedNotes.isEmpty()) return
        viewModelScope.launch {
            val reminder =
                reminderRepository.getReminderForNote(screenState.value.selectedNotes.toList().first()) ?: return@launch
            reminderRepository.deleteReminder(reminder._id)
        }
        switchReminderDialogShow()
    }

    private suspend fun showSnackbar(message: String, icon: ImageVector?) {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = message,
                icon = icon
            )
        )
    }
}
