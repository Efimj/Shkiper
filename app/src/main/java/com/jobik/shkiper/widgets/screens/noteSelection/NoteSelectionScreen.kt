package com.jobik.shkiper.widgets.screens.noteSelection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.ui.components.buttons.FloatingActionButton
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility

@Composable
fun NoteSelectionScreen(
    notesViewModel: NoteSelectionViewModel = hiltViewModel(),
    strictSelection: Boolean = false,
    selectNote: (note: Note?) -> Unit
) {
    val isSearchBarVisible = remember { mutableStateOf(true) }
    val lazyGridNotes = rememberLazyStaggeredGridState()

    /**
     * When user select note
     */
    BackHandler(
        enabled = notesViewModel.screenState.value.selectedNoteId != null, onBack =
        notesViewModel::clearSelectedNote
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        Crossfade(
            targetState = notesViewModel.screenState.value.isNotesInitialized && notesViewModel.screenState.value.notes.isEmpty(),
            label = "animation layouts screen"
        ) { value ->
            if (value) {
                ScreenStub(title = R.string.EmptyNotesPageHeader, icon = Icons.Outlined.Description)
            } else {
                ScreenContent(lazyGridNotes = lazyGridNotes, notesViewModel = notesViewModel)
            }
        }
        Box(modifier = Modifier) {
            com.jobik.shkiper.ui.components.fields.SearchBar(
                isVisible = isSearchBarVisible.value,
                value = notesViewModel.screenState.value.searchText,
                onChange = notesViewModel::changeSearchText
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(35.dp)
                .endWindowInsetsPadding()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
        ) {
            if (strictSelection) {
                AnimatedVisibility(
                    notesViewModel.screenState.value.selectedNoteId != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    FloatingActionButton(
                        icon = Icons.Outlined.Done,
                        notesViewModel.screenState.value.selectedNoteId != null
                    ) {
                        notesViewModel.getSelectedNote()?.let { selectNote(it) }
                    }
                }
            } else {
                FloatingActionButton(
                    icon = if (notesViewModel.screenState.value.selectedNoteId == null) Icons.Outlined.Add else Icons.Outlined.Done,
                ) {
                    selectNote(notesViewModel.getSelectedNote())
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NoteSelectionViewModel,
) {
    val pinnedNotes = notesViewModel.screenState.value.notes.filter { it.isPinned }
    val unpinnedNotes = notesViewModel.screenState.value.notes.filterNot { it.isPinned }

    LazyGridNotes(
        contentPadding = PaddingValues(
            start = 10.dp + startWindowInsetsPadding(),
            top = 10.dp + getSearchBarHeight(),
            end = 10.dp + endWindowInsetsPadding(),
            bottom = 80.dp + bottomWindowInsetsPadding()
        ),
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_list"),
        gridState = lazyGridNotes
    ) {
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
                selected = if (notesViewModel.screenState.value.selectedNoteId != null) setOf(notesViewModel.screenState.value.selectedNoteId!!) else emptySet(),
                onClick = { note ->
                    notesViewModel.clickOnNote(note._id)
                },
                onLongClick = { note ->
                    notesViewModel.clickOnNote(note._id)
                },
            )
        }
        if (unpinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Other)
            notesList(
                notes = unpinnedNotes,
                reminders = notesViewModel.screenState.value.reminders,
                marker = notesViewModel.screenState.value.searchText,
                selected = if (notesViewModel.screenState.value.selectedNoteId != null) setOf(notesViewModel.screenState.value.selectedNoteId!!) else emptySet(),
                onClick = { note ->
                    notesViewModel.clickOnNote(note._id)
                },
                onLongClick = { note ->
                    notesViewModel.clickOnNote(note._id)
                },
            )
        }
    }
}