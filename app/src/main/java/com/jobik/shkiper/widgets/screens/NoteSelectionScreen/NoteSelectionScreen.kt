package com.jobik.shkiper.widgets.screens.NoteSelectionScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.ui.components.buttons.FloatingActionButton
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.fields.SearchBarHeight
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun NoteSelectionScreen(notesViewModel: NoteSelectionViewModel = hiltViewModel(), selectNote: (note: Note) -> Unit) {

    val searchBarHeightPx = with(LocalDensity.current) { SearchBarHeight.dp.roundToPx().toFloat() }

    val searchBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val lazyGridNotes = rememberLazyStaggeredGridState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = searchBarOffsetHeightPx.value + delta
                if (lazyGridNotes.canScrollForward) searchBarOffsetHeightPx.value =
                    newOffset.coerceIn(-searchBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    /**
     * When user select note
     */
    BackHandler(
        enabled = notesViewModel.screenState.value.selectedNoteId != null, onBack =
        notesViewModel::clearSelectedNote
    )

    /**
     * LaunchedEffect for cases when it is impossible to scroll the list.
     */
    LaunchedEffect(lazyGridNotes.canScrollForward, lazyGridNotes.canScrollBackward) {
        if (!lazyGridNotes.canScrollForward && !lazyGridNotes.canScrollBackward) {
            searchBarOffsetHeightPx.value = 0f
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)) {
        if (notesViewModel.screenState.value.isNotesInitialized && notesViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.EmptyNotesPageHeader, icon = Icons.Outlined.Description)
        else
            ScreenContent(lazyGridNotes, notesViewModel)
        Box(modifier = Modifier) {
            com.jobik.shkiper.ui.components.fields.SearchBar(
                searchBarOffsetHeightPx = searchBarOffsetHeightPx.value,
                isVisible = true,
                value = notesViewModel.screenState.value.searchText,
                onChange = notesViewModel::changeSearchText
            )
        }
        Box(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(35.dp)) {
            AnimatedVisibility(
                notesViewModel.screenState.value.selectedNoteId != null,
                enter = fadeIn(tween(200, easing = LinearOutSlowInEasing)),
                exit = fadeOut(tween(200, easing = FastOutSlowInEasing)),
            ) {
                FloatingActionButton(
                    icon = Icons.Outlined.Done,
                    notesViewModel.screenState.value.selectedNoteId != null
                ) {
                    notesViewModel.getSelectedNote()?.let { selectNote(it) }
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
        contentPadding = PaddingValues(10.dp, 70.dp, 10.dp, 80.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_list"),
        gridState = lazyGridNotes
    ) {
        if (notesViewModel.screenState.value.hashtags.isNotEmpty())
            item(span = StaggeredGridItemSpan.FullLine) {
                LazyRow(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
                ) {
                    items(items = notesViewModel.screenState.value.hashtags.toList()) { item ->
                        HashtagButton(item, item == notesViewModel.screenState.value.currentHashtag) {
                            notesViewModel.setCurrentHashtag(
                                item
                            )
                        }
                    }
                }
            }
        if (pinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        stringResource(R.string.Pinned),
                        color = CustomTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
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
                    selected = item._id.toHexString() == notesViewModel.screenState.value.selectedNoteId,
                    onClick = { notesViewModel.clickOnNote(item._id) },
                    onLongClick = { notesViewModel.clickOnNote(item._id) })
            }
        }
        if (unpinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    stringResource(R.string.Other),
                    color = CustomTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
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
                    selected = item._id.toHexString() == notesViewModel.screenState.value.selectedNoteId,
                    onClick = { notesViewModel.clickOnNote(item._id) },
                    onLongClick = { notesViewModel.clickOnNote(item._id) })
            }
        }
    }
}