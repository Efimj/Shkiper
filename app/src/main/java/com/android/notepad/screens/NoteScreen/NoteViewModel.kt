package com.android.notepad.screens.NoteScreen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.notepad.database.data.note.NoteMongoRepository
import com.android.notepad.database.data.reminder.ReminderMongoRepository
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.Reminder
import com.android.notepad.database.models.RepeatMode
import com.android.notepad.helpers.DateHelper
import com.android.notepad.helpers.LinkHelper
import com.android.notepad.navigation.ARGUMENT_NOTE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.realmSetOf
import kotlinx.coroutines.*
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    /*******************
     * Screen States
     *******************/

    private var _isTopAppBarHover = mutableStateOf(false)
    val isTopAppBarHover: State<Boolean> = _isTopAppBarHover

    private var _isBottomAppBarHover = mutableStateOf(false)
    val isBottomAppBarHover: State<Boolean> = _isBottomAppBarHover

    fun setTopAppBarHover(isHover: Boolean) {
        _isTopAppBarHover.value = isHover
    }

    fun setBottomAppBarHover(isHover: Boolean) {
        _isBottomAppBarHover.value = isHover
    }

    /*******************
     * Note States
     *******************/

    private var _noteId = mutableStateOf(ObjectId(savedStateHandle[ARGUMENT_NOTE_ID] ?: ""))
    val noteId: State<ObjectId> = _noteId

    val note = noteRepository.getNote(noteId.value)

    private var _noteHeader = mutableStateOf(note?.header ?: "")
    val noteHeader: State<String> = _noteHeader

    private var _noteBody = mutableStateOf(note?.body ?: "")
    val noteBody: State<String> = _noteBody

    private var _noteIsPinned = mutableStateOf(note?.isPinned ?: false)
    val noteIsPinned: State<Boolean> = _noteIsPinned

    private var _noteUpdatedDate = mutableStateOf(note?.updateDate ?: LocalDateTime.now())
    val noteUpdatedDate: State<LocalDateTime> = _noteUpdatedDate

    private var _noteHashtags = mutableStateOf(note?.hashtags?.toSet() ?: setOf(""))
    val noteHashtags: State<Set<String>> = _noteHashtags

    /*******************
     * Note links handler
     *******************/

    private var _allLinksMetaData = emptySet<LinkHelper.LinkPreview>()
    private val _linksData = mutableStateOf(emptySet<LinkHelper.LinkPreview>())
    val linksData: State<Set<LinkHelper.LinkPreview>> = _linksData

    private val _linksLoading = mutableStateOf(true)
    val linksLoading: State<Boolean> = _linksLoading

    private var linkRefreshTimer: Timer? = null

    private var allLinksMetaData: Set<LinkHelper.LinkPreview>
        get() = _allLinksMetaData
        set(value) {
            _allLinksMetaData = value
            _linksData.value = getCorrectLinks()
        }

    fun runFetchingLinksMetaData() {
        if (linkRefreshTimer == null) {
            fetchLinkMetaData()
            linkRefreshTimer?.cancel()
            linkRefreshTimer = Timer()
            return
        }
        linkRefreshTimer?.cancel()
        linkRefreshTimer = Timer()
        linkRefreshTimer?.schedule(object : TimerTask() {
            override fun run() {
                fetchLinkMetaData()
            }
        }, 1000L)
    }

    private fun fetchLinkMetaData() {
        viewModelScope.launch(Dispatchers.IO) {
            val links = LinkHelper().findLinks(_noteBody.value)
            allLinksMetaData = allLinksMetaData.filter { it.link in links }.toSet()

            val newLinkData = mutableListOf<LinkHelper.LinkPreview>()
            val deferredList = mutableListOf<Deferred<LinkHelper.LinkPreview>>()

            for (link in links) {
                if (allLinksMetaData.any { it.link == link }) continue
                val deferred = async(Dispatchers.IO) {
                    LinkHelper().getOpenGraphData(link)
                }
                deferredList.add(deferred)
            }

            deferredList.awaitAll().forEach { result ->
                newLinkData.add(result)
            }

            allLinksMetaData = allLinksMetaData.plus(newLinkData)
            _linksLoading.value = false
        }
    }

    fun getCorrectLinks(): Set<LinkHelper.LinkPreview> {
        return allLinksMetaData.filterNot { it.title.isNullOrEmpty() && it.description.isNullOrEmpty() && it.img.isNullOrEmpty() }
            .toSet()
    }

    /*******************
     * States for Possible Undo
     *******************/

    data class IntermediateState(val header: String, val body: String)

    private var _intermediateStates = mutableStateOf(listOf(IntermediateState(_noteHeader.value, _noteBody.value)))
    val intermediateStates: State<List<IntermediateState>> = _intermediateStates
    private var _currentStateIndex = mutableStateOf(_intermediateStates.value.size - 1)
    val currentStateIndex: State<Int> = _currentStateIndex

    private var intermediateStatesChangeTimer: Timer? = null

    private fun updateIntermediateStates(intermediateState: IntermediateState) {
        intermediateStatesChangeTimer?.cancel()
        intermediateStatesChangeTimer = Timer()
        intermediateStatesChangeTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (_currentStateIndex.value < _intermediateStates.value.size - 1) {
                    _intermediateStates.value =
                        _intermediateStates.value.subList(0, _currentStateIndex.value + 1).toList()
                }
                if (_intermediateStates.value[_intermediateStates.value.size - 1].header != intermediateState.header || _intermediateStates.value[_intermediateStates.value.size - 1].body != intermediateState.body) {
                    _intermediateStates.value = _intermediateStates.value.plus(intermediateState)
                }
                _currentStateIndex.value = _intermediateStates.value.size - 1
            }
        }, 750L)
    }

    fun noteStateGoNext() {
        if (_currentStateIndex.value >= _intermediateStates.value.size - 1) return
        _currentStateIndex.value++
        changeNoteContent(
            _intermediateStates.value[_currentStateIndex.value].header,
            _intermediateStates.value[_currentStateIndex.value].body
        )
        runFetchingLinksMetaData()
    }

    fun noteStateGoBack() {
        if (_currentStateIndex.value < 1) return
        _currentStateIndex.value--
        changeNoteContent(
            _intermediateStates.value[_currentStateIndex.value].header,
            _intermediateStates.value[_currentStateIndex.value].body
        )
        runFetchingLinksMetaData()
    }

    /*******************
     * Note handlers
     *******************/

    fun updateNoteHeader(text: String) {
        changeNoteHeader(text)
        updateIntermediateStates(IntermediateState(_noteHeader.value, _noteBody.value))
    }

    private fun changeNoteHeader(text: String) {
        _noteHeader.value = text
        _noteUpdatedDate.value = LocalDateTime.now()
        updateNote {
            it.header = this@NoteViewModel._noteHeader.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
        }
    }

    fun updateNoteBody(text: String) {
        changeNoteBody(text)
        updateIntermediateStates(IntermediateState(_noteHeader.value, _noteBody.value))
    }

    private fun changeNoteBody(text: String) {
        _noteBody.value = text
        _noteUpdatedDate.value = LocalDateTime.now()
        updateNote {
            it.body = this@NoteViewModel._noteBody.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
        }
        runFetchingLinksMetaData()
    }

    private fun changeNoteContent(header: String, body: String) {
        _noteHeader.value = header
        _noteBody.value = body
        _noteUpdatedDate.value = LocalDateTime.now()
        updateNote {
            it.header = this@NoteViewModel._noteHeader.value
            it.body = this@NoteViewModel._noteBody.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
        }
    }

    fun switchNotePinnedMode() {
        _noteIsPinned.value = !noteIsPinned.value
        updateNote {
            it.isPinned = this@NoteViewModel.noteIsPinned.value
        }
    }

    fun changeNoteHashtags(hashtags: Set<String>) {
        if (hashtags.isNotEmpty()) {
            _noteHashtags.value = hashtags
            updateNote {
                it.hashtags = realmSetOf(*this@NoteViewModel.noteHashtags.value.toTypedArray())
            }
        } else {
            _noteHashtags.value = emptySet()
            updateNote {
                it.hashtags = realmSetOf()
            }
        }
    }

    private fun updateNote(updateParams: (Note) -> Unit) {
        if (this@NoteViewModel.note == null) return
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(this@NoteViewModel.note._id, application.applicationContext, updateParams)
        }
    }

    fun deleteNoteIfEmpty() {
        viewModelScope.launch {
            if (noteHeader.value.isEmpty() && noteBody.value.isEmpty()) noteRepository.deleteNote(
                _noteId.value,
                application.applicationContext
            )
        }
    }

    fun saveChanges() {
        updateNote {
            it.header = this@NoteViewModel._noteHeader.value
            it.body = this@NoteViewModel._noteBody.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
            it.isPinned = this@NoteViewModel.noteIsPinned.value
        }
    }

    /*******************
     * Reminder region
     *******************/

    private var _reminder = mutableStateOf<Reminder?>(null)
    val reminder: State<Reminder?> = _reminder

    private fun getReminder() {
        viewModelScope.launch {
            val reminderValue = reminderRepository.getReminderForNote(_noteId.value)
            _reminder.value = reminderValue
        }
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                val note = noteRepository.getNote(_noteId.value) ?: return@launch
                val noteList = listOf(note)
                reminderRepository.updateOrCreateReminderForNotes(
                    noteList, application.applicationContext
                ) { updatedReminder ->
                    updatedReminder.date = date
                    updatedReminder.time = time
                    updatedReminder.repeat = repeatMode
                }
                getReminder()
            }
            switchReminderDialogShow()
        }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            val reminderId = reminder.value?._id ?: return@launch
            reminderRepository.deleteReminder(reminderId, application.applicationContext)
            _reminder.value = null
        }
        switchReminderDialogShow()
    }

    private val _isCreateReminderDialogShow = mutableStateOf(false)
    val isCreateReminderDialogShow: State<Boolean> = _isCreateReminderDialogShow

    fun switchReminderDialogShow() {
        _isCreateReminderDialogShow.value = !_isCreateReminderDialogShow.value
    }

    init {
        viewModelScope.launch {
            getReminder()
        }
    }
}