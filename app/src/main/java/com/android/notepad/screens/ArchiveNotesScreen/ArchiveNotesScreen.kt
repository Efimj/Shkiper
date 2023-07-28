package com.android.notepad.screens.ArchiveNotesScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.notepad.R
import com.android.notepad.navigation.AppScreens
import com.android.notepad.ui.components.buttons.HashtagButton
import com.android.notepad.ui.components.cards.NoteCard
import com.android.notepad.ui.components.layouts.CustomTopAppBar
import com.android.notepad.ui.components.layouts.LazyGridNotes
import com.android.notepad.ui.components.layouts.ScreenContentIfNoData
import com.android.notepad.ui.components.layouts.TopAppBarItem
import com.android.notepad.ui.components.modals.CreateReminderDialog
import com.android.notepad.ui.components.modals.ReminderDialogProperties
import com.android.notepad.ui.theme.CustomAppTheme
import com.android.notepad.viewModels.NotesViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArchiveNotesScreen(navController: NavController, archiveViewModel: NotesViewModel = hiltViewModel()) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val searchBarHeight = 60.dp
    val searchBarHeightPx = with(LocalDensity.current) { searchBarHeight.roundToPx().toFloat() }
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

    val actionBarHeight = 56.dp
    val actionBarHeightPx = with(LocalDensity.current) { actionBarHeight.roundToPx().toFloat() }
    val offsetX = remember { Animatable(-actionBarHeightPx) }

    /**
     * LaunchedEffect for cases when the number of selected notes changes.
     */
    LaunchedEffect(archiveViewModel.screenState.value.selectedNotes) {
        if (archiveViewModel.screenState.value.selectedNotes.isEmpty()) {
            offsetX.animateTo(
                targetValue = -actionBarHeightPx, animationSpec = tween(durationMillis = 200)
            )
        } else {
            offsetX.animateTo(
                targetValue = 0f, animationSpec = tween(durationMillis = 200)
            )
        }
    }

    /**
     * LaunchedEffect for cases when it is impossible to scroll the list.
     */
    LaunchedEffect(lazyGridNotes.canScrollForward, lazyGridNotes.canScrollBackward) {
        if (!lazyGridNotes.canScrollForward && !lazyGridNotes.canScrollBackward) {
            searchBarOffsetHeightPx.value = 0f
        }
    }

    /**
     * LaunchedEffect when new note created.
     */
    LaunchedEffect(archiveViewModel.screenState.value.lastCreatedNoteId) {
        if (archiveViewModel.screenState.value.lastCreatedNoteId.isNotEmpty()) {
            navController.navigate(AppScreens.Note.noteId(archiveViewModel.screenState.value.lastCreatedNoteId))
            archiveViewModel.clearLastCreatedNote()
        }
    }

    Box(Modifier.fillMaxSize().nestedScroll(nestedScrollConnection)) {
        if (archiveViewModel.screenState.value.isNotesInitialized && archiveViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(R.string.ArchiveNotesPageHeader, Icons.Outlined.Inbox)
        else
            ScreenContent(lazyGridNotes, archiveViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            com.android.notepad.ui.components.fields.SearchBar(
                searchBarHeight,
                searchBarOffsetHeightPx.value,
                archiveViewModel.screenState.value.selectedNotes.isEmpty(),
                archiveViewModel.screenState.value.searchText,
                archiveViewModel::changeSearchText
            )
            ActionBar(actionBarHeight, offsetX, archiveViewModel)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NotesViewModel,
    currentRoute: String,
    navController: NavController
) {
    LazyGridNotes(
        contentPadding = PaddingValues(10.dp, 70.dp, 10.dp, 80.dp),
        modifier = Modifier.fillMaxSize(),
        gridState = lazyGridNotes
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            LazyRow(
                modifier = Modifier.wrapContentSize(unbounded = true)
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
//        item(span = StaggeredGridItemSpan.FullLine) {
//            Text(
//                stringResource(R.string.Notes),
//                color = CustomAppTheme.colors.textSecondary,
//                style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
//                modifier = Modifier.padding(horizontal = 10.dp)
//            )
//        }
        items(items = notesViewModel.screenState.value.notes) { item ->
            NoteCard(item.header,
                item.body,
                reminder = notesViewModel.screenState.value.reminders.find { it.noteId == item._id },
                markedText = notesViewModel.screenState.value.searchText,
                selected = item._id in notesViewModel.screenState.value.selectedNotes,
                onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
                onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
        }
    }
    if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
        val reminder =
            remember {
                if (notesViewModel.screenState.value.selectedNotes.size == 1)
                    notesViewModel.getReminder(notesViewModel.screenState.value.selectedNotes.first()) else null
            }
        val reminderDialogProperties = remember {
            if (reminder != null) ReminderDialogProperties(reminder.date, reminder.time, reminder.repeat)
            else ReminderDialogProperties()
        }
        CreateReminderDialog(
            reminderDialogProperties = reminderDialogProperties,
            onGoBack = notesViewModel::switchReminderDialogShow,
            onDelete = if (reminder != null) notesViewModel::deleteSelectedReminder else null,
            onSave = notesViewModel::createReminder,
        )

    }
}

@Composable
private fun ActionBar(
    actionBarHeight: Dp, offsetX: Animatable<Float, AnimationVector1D>, notesViewModel: NotesViewModel
) {
    val topAppBarElevation = if (offsetX.value.roundToInt() < -actionBarHeight.value.roundToInt()) 0.dp else 2.dp
    Box(
        modifier = Modifier.height(actionBarHeight).offset { IntOffset(x = 0, y = offsetX.value.roundToInt()) },
    ) {
        CustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            elevation = topAppBarElevation,
            backgroundColor = CustomAppTheme.colors.mainBackground,
            text = notesViewModel.screenState.value.selectedNotes.count().toString(),
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
}

