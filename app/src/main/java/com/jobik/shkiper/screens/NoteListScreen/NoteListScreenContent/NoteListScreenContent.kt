package com.jobik.shkiper.screens.NoteListScreen.NoteListScreenContent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.fields.SearchBar
import com.jobik.shkiper.ui.components.fields.SearchBarActionButton
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.BannerList
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.noteTagsList
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun NoteListScreenContent(
    navController: NavController,
    viewModel: NotesViewModel,
    onSlideNext: () -> Unit,
) {
    val isSearchBarVisible = remember { mutableStateOf(true) }

    BackHandlerIfSelectedNotes(viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        if (viewModel.screenState.value.isNotesInitialized && viewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.EmptyNotesPageHeader, icon = Icons.Outlined.Description)
        else
            NotesListContent(viewModel, navController)
        Box(modifier = Modifier) {
            SearchBar(
                isVisible = viewModel.screenState.value.selectedNotes.isEmpty() && isSearchBarVisible.value,
                value = viewModel.screenState.value.searchText,
                actionButton = SearchBarActionButton(
                    icon = Icons.Outlined.Event,
                    contentDescription = R.string.Reminders,
                    onClick = onSlideNext
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
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val pinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filter { it.isPinned } }
    val unpinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filterNot { it.isPinned } }

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
        item(span = StaggeredGridItemSpan.FullLine) {
            BannerList(navController)
        }
        noteTagsList(
            tags = notesViewModel.screenState.value.hashtags,
            selected = notesViewModel.screenState.value.currentHashtag
        ) { notesViewModel.setCurrentHashtag(it) }
        if (pinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        stringResource(R.string.Pinned),
                        color = AppTheme.colors.textSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(items = pinnedNotes) { item ->
                NoteCard(
                    header = item.header,
                    text = item.body,
                    reminder = rememberNextReminder(
                        reminders = notesViewModel.screenState.value.reminders,
                        noteId = item._id,
                    ),
                    markedText = notesViewModel.screenState.value.searchText,
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
            }
        }
        if (unpinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    stringResource(R.string.Other),
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            items(items = unpinnedNotes) { item ->
                NoteCard(
                    header = item.header,
                    text = item.body,
                    reminder = rememberNextReminder(
                        reminders = notesViewModel.screenState.value.reminders,
                        noteId = item._id,
                    ),
                    markedText = notesViewModel.screenState.value.searchText,
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
            }
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