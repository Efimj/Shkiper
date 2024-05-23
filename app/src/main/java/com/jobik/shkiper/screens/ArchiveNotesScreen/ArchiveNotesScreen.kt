package com.jobik.shkiper.screens.ArchiveNotesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun ArchiveNotesScreen(navController: NavController, archiveViewModel: NotesViewModel = hiltViewModel()) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val isSearchBarVisible = remember { mutableStateOf(true) }
    val lazyGridNotes = rememberLazyStaggeredGridState()

    /**
     * When user select note
     */
    BackHandler(
        enabled = archiveViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        archiveViewModel::clearSelectedNote
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        if (archiveViewModel.screenState.value.isNotesInitialized && archiveViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.ArchiveNotesPageHeader, icon = Icons.Outlined.Inbox)
        else
            ScreenContent(lazyGridNotes, archiveViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            com.jobik.shkiper.ui.components.fields.SearchBar(
                isVisible = archiveViewModel.screenState.value.selectedNotes.isEmpty() && isSearchBarVisible.value,
                value = archiveViewModel.screenState.value.searchText,
                onChange = archiveViewModel::changeSearchText
            )
            ActionBar(
                isVisible = archiveViewModel.screenState.value.selectedNotes.isNotEmpty(),
                notesViewModel = archiveViewModel
            )
        }
    }
}

@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NotesViewModel,
    currentRoute: String,
    navController: NavController
) {
    LazyGridNotes(
        contentPadding = PaddingValues(
            start = 10.dp + startWindowInsetsPadding(),
            top = getSearchBarHeight() + 10.dp,
            end = 10.dp + endWindowInsetsPadding(),
            bottom = 80.dp + bottomWindowInsetsPadding()
        ),
        modifier = Modifier.fillMaxSize(),
        gridState = lazyGridNotes
    ) {
        noteTagsList(
            tags = notesViewModel.screenState.value.hashtags,
            selected = notesViewModel.screenState.value.currentHashtag
        ) { notesViewModel.setCurrentHashtag(it) }
        notesList(
            notes = notesViewModel.screenState.value.notes,
            reminders = notesViewModel.screenState.value.reminders,
            marker = notesViewModel.screenState.value.searchText,
            selected = notesViewModel.screenState.value.selectedNotes,
            onClick = { note ->
                notesViewModel.clickOnNote(
                    note = note,
                    currentRoute = currentRoute,
                    navController = navController
                )
            },
            onLongClick = { note ->
                notesViewModel.toggleSelectedNoteCard(
                    noteId = note._id
                )
            }
        )
    }
    if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
        if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
            CreateReminderDialog(
                reminderDialogProperties = ReminderDialogProperties(),
                onGoBack = notesViewModel::switchReminderDialogShow,
                onDelete = null,
                onSave = notesViewModel::createReminder,
            )
        }
    }
}

@Composable
private fun ActionBar(
    isVisible: Boolean,
    notesViewModel: NotesViewModel
) {
    CustomTopAppBar(
        isVisible = isVisible,
        counter = notesViewModel.screenState.value.selectedNotes.count(),
        navigation = TopAppBarItem(
            isActive = false,
            icon = Icons.Default.Close,
            iconDescription = R.string.GoBack,
            onClick = notesViewModel::clearSelectedNote
        ),
        items = listOf(
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.PushPin,
                iconDescription = R.string.AttachNote,
                onClick = notesViewModel::pinSelectedNotes
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.NotificationAdd,
                iconDescription = R.string.AddToNotification,
                onClick = notesViewModel::switchReminderDialogShow
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.Unarchive,
                iconDescription = R.string.AddToArchive,
                onClick = notesViewModel::unarchiveSelectedNotes
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.Delete,
                iconDescription = R.string.AddToBasket,
                onClick = notesViewModel::moveSelectedNotesToBasket
            ),
        )
    )
}

