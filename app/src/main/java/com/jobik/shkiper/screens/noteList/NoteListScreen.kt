package com.jobik.shkiper.screens.noteList

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.navigateToSecondary
import com.jobik.shkiper.navigation.Screen
import com.jobik.shkiper.ui.components.cards.DonateBannerProvider
import com.jobik.shkiper.ui.components.fields.SearchBar
import com.jobik.shkiper.ui.components.fields.SearchBarActionButton
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenStub
import com.jobik.shkiper.ui.components.layouts.noteTagsList
import com.jobik.shkiper.ui.components.layouts.notesList
import com.jobik.shkiper.ui.components.layouts.notesListHeadline
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.helpers.LocalNotePosition
import com.jobik.shkiper.ui.helpers.LocalSharedElementKey
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.settings.SettingsHandler.checkIsDonateBannerNeeded
import com.jobik.shkiper.util.settings.SettingsManager
import com.jobik.shkiper.util.settings.SettingsManager.settings
import java.time.LocalDateTime

@Composable
fun NoteList(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val isSearchBarVisible = remember { mutableStateOf(true) }

    BackHandlerIfSelectedNotes(viewModel)
    UpdateNoteType(viewModel)
    BasketSnackBar(viewModel)
    BasketDelitionDialog(viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        Crossfade(
            targetState = viewModel.screenState.value.isNotesInitialized && viewModel.screenState.value.notes.isEmpty(),
            label = "animation layouts screen"
        ) { value ->
            if (value) {
                ScreenStub(
                    icon = when (viewModel.screenState.value.currentNotes) {
                        NotePosition.MAIN -> R.drawable.note_stack
                        NotePosition.ARCHIVE -> R.drawable.inbox
                        NotePosition.DELETE -> R.drawable.delete
                    },
                    title = when (viewModel.screenState.value.currentNotes) {
                        NotePosition.MAIN -> R.string.Notes
                        NotePosition.ARCHIVE -> R.string.Archive
                        NotePosition.DELETE -> R.string.Basket
                    },
                    description = R.string.empty_notes_stub_description,
                )
            } else {
                NotesListContent(
                    notesViewModel = viewModel,
                    navController = navController,
                )
            }
        }
        Box(modifier = Modifier) {
            SearchBar(
                isVisible = viewModel.screenState.value.selectedNotes.isEmpty() && isSearchBarVisible.value,
                value = viewModel.screenState.value.searchText,
                actionButton = SearchBarActionButton(
                    icon = Icons.Outlined.Event,
                    contentDescription = R.string.Reminders,
                    onClick = {
                        navController.navigateToSecondary(Screen.Calendar)
                    }
                ),
                onChange = viewModel::changeSearchText,
            )
            NoteListScreenActionBar(
                isVisible = viewModel.screenState.value.selectedNotes.isNotEmpty(),
                notesViewModel = viewModel
            )
        }
    }

    NoteListScreenReminderCheck(viewModel)
    CreateReminderContent(viewModel)
}

@Composable
private fun BasketDelitionDialog(viewModel: NotesViewModel) {
    if (viewModel.screenState.value.isDeleteNotesDialogShow)
        ActionDialog(
            title = stringResource(R.string.DeleteSelectedNotesDialogText),
            icon = Icons.Outlined.Warning,
            confirmText = stringResource(R.string.Confirm),
            onConfirm = viewModel::deleteSelectedNotes,
            goBackText = stringResource(R.string.Cancel),
            onGoBack = viewModel::switchDeleteDialogShow
        )
}

@Composable
private fun BasketSnackBar(viewModel: NotesViewModel) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.screenState.value.currentNotes) {
        if (viewModel.screenState.value.currentNotes == NotePosition.DELETE) {
            SnackbarHostUtil.snackbarHostState.showSnackbar(
                SnackbarVisualsCustom(
                    message = context.getString(R.string.BasketPageHeader),
                    icon = Icons.Outlined.DeleteSweep,
                    duration = SnackbarDuration.Indefinite
                )
            )
        }
    }
}

@Composable
private fun UpdateNoteType(viewModel: NotesViewModel) {
    val notePosition = LocalNotePosition.current

    LaunchedEffect(notePosition) {
        viewModel.updateNotePosition(notePosition = notePosition)
    }
}

@Composable
private fun BackHandlerIfSelectedNotes(notesViewModel: NotesViewModel) {
    /**
     * When user select note
     */
    BackHandler(
        enabled = notesViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        notesViewModel::clearSelectedNote
    )
}

@Composable
private fun NotesListContent(
    notesViewModel: NotesViewModel,
    navController: NavController,
) {
    val sharedOrigin = LocalSharedElementKey.current
    val context = LocalContext.current

    val pinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filter { it.isPinned } }
    val unpinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filterNot { it.isPinned } }

    var showDonateBanner by rememberSaveable {
        mutableStateOf(checkIsDonateBannerNeeded(settings))
    }

    LazyGridNotes(
        contentPadding = PaddingValues(
            start = 10.dp + startWindowInsetsPadding(),
            top = getSearchBarHeight() + 10.dp,
            end = 10.dp + endWindowInsetsPadding(),
            bottom = 80.dp + bottomWindowInsetsPadding()
        ),
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_list"),
    ) {
        if (showDonateBanner) {
            item(span = StaggeredGridItemSpan.FullLine) {
                DonateBannerProvider(
                    onClick = {
                        SettingsManager.update(
                            context = context,
                            settings = settings.copy(lastShowingDonateBanner = LocalDateTime.now())
                        )
                        navController.navigateToSecondary(Screen.Purchases)
                        showDonateBanner = false
                    },
                    onDismiss = {
                        SettingsManager.update(
                            context = context,
                            settings = settings.copy(lastShowingDonateBanner = LocalDateTime.now())
                        )
                        showDonateBanner = false
                    },
                )
            }
        }
        noteTagsList(
            tags = notesViewModel.screenState.value.hashtags,
            selected = notesViewModel.screenState.value.currentHashtag
        ) { notesViewModel.setCurrentHashtag(it) }
        if (pinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Pinned)
            notesList(
                notes = pinnedNotes,
                reminders = notesViewModel.screenState.value.reminders,
                marker = notesViewModel.screenState.value.searchText,
                selected = notesViewModel.screenState.value.selectedNotes,
                onClick = { note ->
                    notesViewModel.clickOnNote(
                        note = note,
                        onNavigate = {
                            navController.navigateToSecondary(
                                Screen.Note(
                                    id = note._id.toHexString(),
                                    sharedElementOrigin = sharedOrigin
                                )
                            )
                        })
                },
                onLongClick = { note ->
                    notesViewModel.toggleSelectedNoteCard(
                        noteId = note._id
                    )
                },
            )
        }
        if (unpinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Other)
            notesList(
                notes = unpinnedNotes,
                reminders = notesViewModel.screenState.value.reminders,
                marker = notesViewModel.screenState.value.searchText,
                selected = notesViewModel.screenState.value.selectedNotes,
                onClick = { note ->
                    notesViewModel.clickOnNote(
                        note = note,
                        onNavigate = {
                            navController.navigateToSecondary(
                                Screen.Note(
                                    id = note._id.toHexString(),
                                    sharedElementOrigin = sharedOrigin
                                )
                            )
                        })
                },
                onLongClick = { note ->
                    notesViewModel.toggleSelectedNoteCard(
                        noteId = note._id
                    )
                },
            )
        }
    }
}

@Composable
private fun CreateReminderContent(notesViewModel: NotesViewModel) {
    if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
        CreateReminderDialog(
            reminderDialogProperties = ReminderDialogProperties(),
            onGoBack = notesViewModel::switchReminderDialogShow,
            onDelete = null,
            onSave = notesViewModel::createReminder,
        )
    }
}