package com.android.notepad.screens.NoteListScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.notepad.R
import com.android.notepad.database.models.Note
import com.android.notepad.navigation.AppScreens
import com.android.notepad.ui.components.cards.NoteCard
import com.android.notepad.ui.components.fields.SearchBar
import com.android.notepad.ui.components.layouts.LazyGridNotes
import com.android.notepad.ui.theme.CustomAppTheme
import kotlin.math.roundToInt
import com.android.notepad.ui.components.buttons.CreateNoteButton
import com.android.notepad.ui.components.buttons.HashtagButton
import com.android.notepad.ui.components.modals.CreateReminderDialog
import com.android.notepad.ui.components.modals.ReminderDialogProperties
import com.android.notepad.ui.modifiers.circularRotation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
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
    LaunchedEffect(notesViewModel.screenState.value.lastCreatedNoteId) {
        if (notesViewModel.screenState.value.lastCreatedNoteId.isNotEmpty()) {
            navController.navigate(AppScreens.Note.noteId(notesViewModel.screenState.value.lastCreatedNoteId))
            notesViewModel.clearLastCreatedNote()
        }
    }

    Box(Modifier.fillMaxSize().nestedScroll(nestedScrollConnection)) {
        if (notesViewModel.screenState.value.isNotesInitialized && notesViewModel.screenState.value.notes.isEmpty())
            ScreenIfNoData()
        else
            ScreenContent(lazyGridNotes, notesViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            SearchBar(searchBarHeight, searchBarOffsetHeightPx, notesViewModel)
            ActionBar(actionBarHeight, offsetX, notesViewModel)
        }
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(35.dp)) {
            AnimatedContent(notesViewModel.screenState.value.selectedNotes.isEmpty()) {
                CreateNoteButton(notesViewModel.screenState.value.selectedNotes.isEmpty()) {
                    notesViewModel.createNewNote()
                }
            }
        }
    }
}

@Composable
private fun ScreenIfNoData() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = CustomAppTheme.colors.active,
                modifier = Modifier.size(140.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.EmptyNotesPageHeader),
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = CustomAppTheme.colors.text
            )
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
    val pinnedNotes = notesViewModel.screenState.value.notes.filter { it.isPinned }
    val unpinnedNotes = notesViewModel.screenState.value.notes.filterNot { it.isPinned }

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
        if (pinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        stringResource(R.string.Pinned),
                        color = CustomAppTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(items = pinnedNotes) { item ->
                NoteCard(item.header,
                    item.body,
                    reminder = notesViewModel.screenState.value.reminders.find { it.noteId == item._id },
                    markedText = notesViewModel.screenState.value.searchText,
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { onNoteClick(notesViewModel, item, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
            }
        }
        if (unpinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    stringResource(R.string.Other),
                    color = CustomAppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            items(items = unpinnedNotes) { item ->
                NoteCard(item.header,
                    item.body,
                    reminder = notesViewModel.screenState.value.reminders.find { it.noteId == item._id },
                    markedText = notesViewModel.screenState.value.searchText,
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { onNoteClick(notesViewModel, item, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
            }
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
private fun AnimatedContent(state: Boolean, content: @Composable (() -> Unit)) {
    AnimatedVisibility(
        state,
        enter = fadeIn(tween(200, easing = LinearOutSlowInEasing)),
        exit = fadeOut(tween(200, easing = FastOutSlowInEasing)),
    ) {
        content()
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
        androidx.compose.material.TopAppBar(
            elevation = topAppBarElevation,
            contentColor = CustomAppTheme.colors.textSecondary,
            backgroundColor = CustomAppTheme.colors.mainBackground,
            title = {
                Text(
                    notesViewModel.screenState.value.selectedNotes.count().toString(),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                    color = CustomAppTheme.colors.textSecondary,
                    maxLines = 1,
                )
            },
            navigationIcon = {
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { notesViewModel.clearSelectedNote() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.GoBack),
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
            },
            actions = {
                androidx.compose.material.IconButton(
                    onClick = { notesViewModel.pinSelectedNotes() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PushPin,
                        contentDescription = stringResource(R.string.AttachNote),
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { notesViewModel.switchReminderDialogShow() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationAdd,
                        contentDescription = stringResource(R.string.AddToNotification),
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Archive,
                        contentDescription = stringResource(R.string.AddToArchive),
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { notesViewModel.deleteSelectedNotes() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.AddToBasket),
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SearchBar(
    searchBarHeight: Dp, searchBarOffsetHeightPx: MutableState<Float>, notesViewModel: NotesViewModel
) {
    val searchBarFloatHeight = with(LocalDensity.current) { searchBarHeight.roundToPx().toFloat() }

    AnimatedVisibility(
        notesViewModel.screenState.value.selectedNotes.isEmpty(),
        enter = slideIn(tween(200, easing = LinearOutSlowInEasing)) {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
        exit = slideOut(tween(200, easing = FastOutSlowInEasing)) {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
    ) {
        Box(
            modifier = Modifier.height(searchBarHeight).padding(20.dp, 10.dp, 20.dp, 0.dp)
                .offset { IntOffset(x = 0, y = searchBarOffsetHeightPx.value.roundToInt()) },
        ) {
            SearchBar(search = notesViewModel.screenState.value.searchText,
                onTrailingIconClick = { notesViewModel.changeSearchText("") },
                onValueChange = { notesViewModel.changeSearchText(it) })
        }
    }
}

private fun onNoteClick(
    notesViewModel: NotesViewModel, it: Note, currentRoute: String, navController: NavController
) {
    if (notesViewModel.screenState.value.selectedNotes.isNotEmpty()) notesViewModel.toggleSelectedNoteCard(it._id)
    else {
        if (currentRoute.substringBefore("/") != AppScreens.Note.route.substringBefore("/")) {
            navController.navigate(AppScreens.Note.noteId(it._id.toHexString()))
        }
    }
}

