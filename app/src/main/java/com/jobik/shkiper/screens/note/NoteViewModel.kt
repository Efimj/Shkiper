package com.jobik.shkiper.screens.note

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.helpers.DateHelper.Companion.sortReminders
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.helpers.LinkHelper
import com.jobik.shkiper.navigation.Argument_Note_Id
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.widgets.handlers.handleNoteWidgetPin
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.realmSetOf
import kotlinx.coroutines.*
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

data class NoteScreenState(
    val isLoading: Boolean = true,
    val isGoBack: Boolean = false,
    val isTopAppBarHover: Boolean = false,
    val isBottomAppBarHover: Boolean = false,
    val isStylingEnabled: Boolean = false,
    val isStyling: Boolean = false,
    val showShareDialog: Boolean = false,
    val showShareNoteDialog: Boolean = false,

    val noteId: ObjectId = ObjectId(),
    val noteHeader: String = "",
    val notePosition: NotePosition = NotePosition.MAIN,
    val noteBody: String = "",
    val isPinned: Boolean = false,
    val linkPreviewEnabled: Boolean = false,
    val updatedDate: LocalDateTime = LocalDateTime.now(),
    val hashtags: Set<String> = emptySet(),
    val deletionDate: LocalDateTime? = LocalDateTime.now(),

    val linksLoading: Boolean = false,
    val linksMetaData: Set<LinkHelper.LinkPreview> = emptySet(),
    val intermediateStates: List<NoteViewModel.IntermediateState> = listOf(
        NoteViewModel.IntermediateState(noteHeader, noteBody)
    ),
    val currentIntermediateIndex: Int = intermediateStates.size - 1,
    val reminders: List<Reminder> = emptyList(),
    val isReminderMenuOpen: Boolean = false,
    val isDeleteDialogShow: Boolean = false,
    val allHashtags: Set<String> = emptySet(),
)

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _screenState = mutableStateOf(NoteScreenState())
    val screenState: State<NoteScreenState> = _screenState

    init {
        initializeNote(ObjectId(savedStateHandle[Argument_Note_Id] ?: ""))
        viewModelScope.launch {
            getReminders()
            getHashtags()
        }
    }

    private fun initializeNote(noteId: ObjectId) {
        val note = noteRepository.getNote(noteId)
        if (note == null)
            _screenState.value = _screenState.value.copy(
                isLoading = false,
                isGoBack = true
            )
        else
            _screenState.value = _screenState.value.copy(
                isLoading = false,
                noteId = noteId,
                noteHeader = note.header,
                noteBody = note.body,
                isPinned = note.isPinned,
                updatedDate = note.updateDate,
                hashtags = note.hashtags,
                notePosition = note.position,
                deletionDate = note.deletionDate,
                linkPreviewEnabled = note.linkPreviewEnabled,
                intermediateStates = listOf(IntermediateState(note.header, note.body))
            )
    }

    fun goBackScreen() {
        _screenState.value = _screenState.value.copy(
            isLoading = false,
            isGoBack = true
        )
    }

    /*******************
     * Screen States
     *******************/

    fun setTopAppBarHover(isHover: Boolean) {
        _screenState.value = _screenState.value.copy(isTopAppBarHover = isHover)
    }

    fun setBottomAppBarHover(isHover: Boolean) {
        _screenState.value = _screenState.value.copy(isBottomAppBarHover = isHover)
    }

    fun switchDeleteDialogShow() {
        _screenState.value =
            _screenState.value.copy(isDeleteDialogShow = !_screenState.value.isDeleteDialogShow)
    }

    fun switchStyling(mode: Boolean? = null) {
        if (mode !== null) _screenState.value = _screenState.value.copy(isStyling = mode)
        else
            _screenState.value = _screenState.value.copy(isStyling = !_screenState.value.isStyling)
    }

    fun switchStylingEnabled(mode: Boolean? = null) {
        if (mode !== null) _screenState.value = _screenState.value.copy(isStylingEnabled = mode)
        else
            _screenState.value =
                _screenState.value.copy(isStylingEnabled = !_screenState.value.isStylingEnabled)
    }

    fun switchShowShareDialog(mode: Boolean? = null) {
        if (mode !== null) _screenState.value = _screenState.value.copy(showShareDialog = mode)
        else
            _screenState.value =
                _screenState.value.copy(showShareDialog = !_screenState.value.showShareDialog)
    }

    fun switchShowShareNoteDialog(mode: Boolean? = null) {
        if (mode !== null) _screenState.value = _screenState.value.copy(showShareNoteDialog = mode)
        else
            _screenState.value =
                _screenState.value.copy(showShareNoteDialog = !_screenState.value.showShareNoteDialog)
    }

    /*******************
     * Note links handler
     *******************/

    private var _allLinksMetaData = emptySet<LinkHelper.LinkPreview>()
    private var linkRefreshTimer: Timer? = null

    private var allLinksMetaData: Set<LinkHelper.LinkPreview>
        get() = _allLinksMetaData
        set(value) {
            _allLinksMetaData = value
            _screenState.value = _screenState.value.copy(linksMetaData = getCorrectLinks())
        }

    fun switchLinkPreviewEnabled(mode: Boolean? = null) {
        val newState = mode ?: !_screenState.value.linkPreviewEnabled
        _screenState.value = _screenState.value.copy(linkPreviewEnabled = newState)
        updateNote {
            it.linkPreviewEnabled = newState
        }

        refreshLinks()
    }

    fun refreshLinks() {
        linkRefreshTimer?.cancel()
        allLinksMetaData = emptySet()
        runFetchingLinksMetaData()
    }

    private fun runFetchingLinksMetaData() {
        _screenState.value = _screenState.value.copy(linksLoading = true)

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
        }, 300L)
    }

    private fun fetchLinkMetaData() {
        if (_screenState.value.linkPreviewEnabled) {
            viewModelScope.launch(Dispatchers.IO) {
                val richTextState = RichTextState()
                richTextState.setHtml(_screenState.value.noteBody)
                val links = LinkHelper().findLinks(richTextState.toMarkdown())

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
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val richTextState = RichTextState()
                richTextState.setHtml(_screenState.value.noteBody)
                val links = LinkHelper().findLinks(richTextState.toMarkdown())

                allLinksMetaData =
                    links.map { link -> LinkHelper.LinkPreview(link = link, description = link) }
                        .toSet()
            }
        }
        _screenState.value = _screenState.value.copy(linksLoading = false)
    }

    fun getCorrectLinks(): Set<LinkHelper.LinkPreview> {
        return allLinksMetaData.filterNot { it.title.isNullOrEmpty() && it.description.isNullOrEmpty() && it.img.isNullOrEmpty() }
            .toSet()
    }

    /*******************
     * States for Possible Undo
     *******************/

    data class IntermediateState(val header: String, val body: String)

    private var intermediateStatesChangeTimer: Timer? = null

    private fun updateIntermediateStates(intermediateState: IntermediateState) {
        intermediateStatesChangeTimer?.cancel()
        intermediateStatesChangeTimer = Timer()
        intermediateStatesChangeTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (_screenState.value.currentIntermediateIndex < _screenState.value.intermediateStates.size - 1) {
                    _screenState.value =
                        _screenState.value.copy(
                            intermediateStates = _screenState.value.intermediateStates.subList(
                                0,
                                _screenState.value.currentIntermediateIndex + 1
                            ).toList()
                        )
                }
                if (_screenState.value.intermediateStates[_screenState.value.intermediateStates.size - 1].header != intermediateState.header ||
                    _screenState.value.intermediateStates[_screenState.value.intermediateStates.size - 1].body != intermediateState.body
                ) {
                    _screenState.value = _screenState.value.copy(
                        intermediateStates = _screenState.value.intermediateStates.plus(
                            intermediateState
                        )
                    )
                }
                _screenState.value =
                    _screenState.value.copy(currentIntermediateIndex = _screenState.value.intermediateStates.size - 1)
            }
        }, 750L)
    }

    fun noteStateGoNext() {
        if (_screenState.value.currentIntermediateIndex >= _screenState.value.intermediateStates.size - 1) return
        _screenState.value =
            _screenState.value.copy(currentIntermediateIndex = _screenState.value.currentIntermediateIndex + 1)
        changeNoteContent(
            _screenState.value.intermediateStates[_screenState.value.currentIntermediateIndex].header,
            _screenState.value.intermediateStates[_screenState.value.currentIntermediateIndex].body
        )
        runFetchingLinksMetaData()
    }

    fun noteStateGoBack() {
        if (_screenState.value.currentIntermediateIndex < 1) return
        _screenState.value =
            _screenState.value.copy(currentIntermediateIndex = _screenState.value.currentIntermediateIndex - 1)
        changeNoteContent(
            _screenState.value.intermediateStates[_screenState.value.currentIntermediateIndex].header,
            _screenState.value.intermediateStates[_screenState.value.currentIntermediateIndex].body
        )
        runFetchingLinksMetaData()
    }

    /*******************
     * Note handlers
     *******************/

    fun createWidget() {
        viewModelScope.launch {
            handleNoteWidgetPin(
                application.applicationContext,
                screenState.value.noteId.toHexString()
            )
        }
    }

    fun updateNoteHeader(text: String) {
        changeNoteHeader(text)
        updateIntermediateStates(
            IntermediateState(
                _screenState.value.noteHeader,
                _screenState.value.noteBody
            )
        )
    }

    private fun changeNoteHeader(text: String) {
        _screenState.value =
            _screenState.value.copy(noteHeader = text, updatedDate = LocalDateTime.now())
        updateNote {
            it.header = this@NoteViewModel._screenState.value.noteHeader
            it.updateDate = this@NoteViewModel._screenState.value.updatedDate
        }
    }

    fun updateNoteBody(text: String) {
        changeNoteBody(text)
        updateIntermediateStates(
            IntermediateState(
                _screenState.value.noteHeader,
                _screenState.value.noteBody
            )
        )
    }

    private fun changeNoteBody(text: String) {
        _screenState.value =
            _screenState.value.copy(noteBody = text, updatedDate = LocalDateTime.now())
        updateNote {
            it.body = this@NoteViewModel._screenState.value.noteBody
            it.updateDate = this@NoteViewModel._screenState.value.updatedDate
        }
        runFetchingLinksMetaData()
    }

    private fun changeNoteContent(header: String, body: String) {
        _screenState.value =
            _screenState.value.copy(
                noteHeader = header,
                noteBody = body,
                updatedDate = LocalDateTime.now()
            )
        updateNote {
            it.header = this@NoteViewModel._screenState.value.noteHeader
            it.body = this@NoteViewModel._screenState.value.noteBody
            it.updateDate = this@NoteViewModel._screenState.value.updatedDate
        }
    }

    fun switchNotePinnedMode() {
        _screenState.value = _screenState.value.copy(isPinned = !_screenState.value.isPinned)
        if (_screenState.value.notePosition == NotePosition.ARCHIVE) {
            val newPosition = NotePosition.MAIN
            updateNote {
                it.isPinned = this@NoteViewModel._screenState.value.isPinned
                it.position = newPosition
            }
            _screenState.value = _screenState.value.copy(
                notePosition = newPosition,
                isGoBack = true,
            )
        } else {
            updateNote {
                it.isPinned = this@NoteViewModel._screenState.value.isPinned
            }
        }
    }

    fun changeNoteHashtags(hashtags: Set<String>) {
        _screenState.value = _screenState.value.copy(hashtags = hashtags)
        updateNote {
            it.hashtags = realmSetOf(*hashtags.toTypedArray())
        }
    }

    private fun updateNote(updateParams: (Note) -> Unit) {
        if (_screenState.value.isLoading) return
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(this@NoteViewModel._screenState.value.noteId, updateParams)
        }
    }

    fun deleteNoteIfEmpty(body: String) {
        viewModelScope.launch {
            if (_screenState.value.noteHeader.isEmpty() && body.isEmpty()) {
                goBackScreen()
                noteRepository.deleteNote(
                    _screenState.value.noteId,
                )
            }
        }
    }

    fun shareNoteText(context: Context, text: String) {
        val sharedText = _screenState.value.noteHeader + "\n" + text
        IntentHelper().shareTextIntent(context, sharedText)
    }

    fun archiveNote() {
        viewModelScope.launch {
            val newPosition = NotePosition.ARCHIVE
            noteRepository.updateNote(screenState.value.noteId) { updatedNote ->
                updatedNote.position = newPosition
                updatedNote.isPinned = false
            }
            _screenState.value = _screenState.value.copy(
                notePosition = newPosition,
                isGoBack = true,
            )
            // when returning, the snackbar turns off
//            showSnackbar(
//                message = application.applicationContext.getString(R.string.NoteArchived),
//                icon = Icons.Default.Archive
//            )
        }
    }

    fun unarchiveNote() {
        viewModelScope.launch {
            val newPosition = NotePosition.MAIN
            noteRepository.updateNote(screenState.value.noteId) { updatedNote ->
                updatedNote.position = newPosition
            }
            _screenState.value = _screenState.value.copy(
                notePosition = newPosition,
                isGoBack = true,
            )
            // when returning, the snackbar turns off
//            showSnackbar(
//                message = application.applicationContext.getString(R.string.NoteUnarchived),
//                icon = Icons.Default.Unarchive
//            )
        }
    }

    fun moveToBasket() {
        viewModelScope.launch {
            val newPosition = NotePosition.DELETE
            noteRepository.updateNote(screenState.value.noteId) { updatedNote ->
                updatedNote.position = newPosition
                updatedNote.isPinned = false
                updatedNote.deletionDate = LocalDateTime.now()
            }
            _screenState.value = _screenState.value.copy(
                notePosition = newPosition,
                isPinned = false,
                isGoBack = true,
            )
        }
    }

    fun removeNoteFromBasket() {
        viewModelScope.launch {
            val newPosition = NotePosition.MAIN
            noteRepository.updateNote(screenState.value.noteId) { updatedNote ->
                updatedNote.position = newPosition
                updatedNote.deletionDate = null
            }
            _screenState.value = _screenState.value.copy(
                notePosition = newPosition,
                isGoBack = true,
            )
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            noteRepository.deleteNote(screenState.value.noteId)
            _screenState.value = _screenState.value.copy(
                isGoBack = true,
            )
        }
    }

    /*******************
     * Reminder region
     *******************/

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getRemindersForNote(_screenState.value.noteId).collect {
                _screenState.value = _screenState.value.copy(reminders = sortReminders(it))
            }
        }
    }

    fun createOrUpdateReminder(
        reminder: Reminder?,
        date: LocalDate,
        time: LocalTime,
        repeatMode: RepeatMode
    ) {
        if (!DateHelper.isFutureDateTime(date, time)) return

        viewModelScope.launch {
            val updateFunction: (Reminder) -> Unit = {
                it.date = date
                it.time = time
                it.repeat = repeatMode
            }

            val note = noteRepository.getNote(_screenState.value.noteId) ?: return@launch

            if (reminder != null) {
                reminderRepository.updateReminder(reminder._id, note, updateFunction)
            } else {
                reminderRepository.createReminderForNotes(listOf(note), updateFunction)
            }
        }
    }

    fun deleteReminder(reminderId: ObjectId) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminderId)
        }
    }

    fun deleteReminder(reminderIds: List<ObjectId>) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminderIds)
        }
    }

    fun switchReminderDialogShow() {
        _screenState.value =
            _screenState.value.copy(isReminderMenuOpen = !_screenState.value.isReminderMenuOpen)
    }

    fun getHashtags() {
        _screenState.value = _screenState.value.copy(allHashtags = noteRepository.getHashtags())
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