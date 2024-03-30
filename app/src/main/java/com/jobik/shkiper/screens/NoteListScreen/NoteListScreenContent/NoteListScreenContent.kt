package com.jobik.shkiper.screens.NoteListScreen.NoteListScreenContent

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.fields.SearchBar
import com.jobik.shkiper.ui.components.fields.SearchBarActionButton
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.BannerList
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun NoteListScreenContent(
    navController: NavController,
    viewModel: NotesViewModel,
    onSlideNext: () -> Unit,
) {
    val actionBarHeight = 56.dp

    val lazyGridNotes = rememberLazyStaggeredGridState()

    val isSearchBarVisible = remember { mutableStateOf(true) }

    val actionBarHeightPx = with(LocalDensity.current) { actionBarHeight.roundToPx().toFloat() }
    val offsetX = remember { Animatable(-actionBarHeightPx) }

    BackHandlerIfSelectedNotes(viewModel)
    IfSelectedNotesChanged(viewModel, offsetX, actionBarHeightPx)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.mainBackground)
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        if (viewModel.screenState.value.isNotesInitialized && viewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.EmptyNotesPageHeader, icon = Icons.Outlined.Description)
        else
            NotesListContent(viewModel, lazyGridNotes, navController)
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
            NoteListScreenActionBar(actionBarHeight, offsetX, viewModel)
        }
    }

    NoteListScreenReminderCheck(viewModel)
    CreateReminderContent(viewModel)
}

@Composable
private fun IfSelectedNotesChanged(
    notesViewModel: NotesViewModel,
    offsetX: Animatable<Float, AnimationVector1D>,
    actionBarHeightPx: Float
) {
    /**
     * LaunchedEffect for cases when the number of selected notes changes.
     */
    LaunchedEffect(notesViewModel.screenState.value.selectedNotes) {
        if (notesViewModel.screenState.value.selectedNotes.isEmpty()) {
            offsetX.animateTo(
                targetValue = -actionBarHeightPx, animationSpec = tween(durationMillis = 200)
            )
        } else {
            offsetX.animateTo(
                targetValue = 0f, animationSpec = tween(durationMillis = 200)
            )
        }
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
    lazyGridNotes: LazyStaggeredGridState,
    navController: NavController,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val pinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filter { it.isPinned } }
    val unpinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filterNot { it.isPinned } }

    LazyGridNotes(
        contentPadding = PaddingValues(10.dp, getSearchBarHeight() + 10.dp, 10.dp, 80.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_list"),
        gridState = lazyGridNotes
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            BannerList(navController)
        }
        if (notesViewModel.screenState.value.hashtags.isNotEmpty())
            item(span = StaggeredGridItemSpan.FullLine) {
                LazyRow(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
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