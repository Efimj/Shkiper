package com.jobik.shkiper.screens.NoteListScreen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.helpers.areChanelNotificationsEnabled
import com.jobik.shkiper.helpers.areEXACTNotificationsEnabled
import com.jobik.shkiper.helpers.areNotificationsEnabled
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.services.notification_service.NotificationScheduler
import com.jobik.shkiper.ui.components.cards.NoteCard
import kotlin.math.roundToInt
import com.jobik.shkiper.ui.components.buttons.FloatingActionButton
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.viewmodels.NotesViewModel
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom

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
     * When user select note
     */
    BackHandler(
        enabled = notesViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        notesViewModel::clearSelectedNote
    )

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

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        if (notesViewModel.screenState.value.isNotesInitialized && notesViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(R.string.EmptyNotesPageHeader, Icons.Outlined.Description)
        else
            ScreenContent(lazyGridNotes, notesViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            com.jobik.shkiper.ui.components.fields.SearchBar(
                searchBarHeight,
                searchBarOffsetHeightPx.value,
                notesViewModel.screenState.value.selectedNotes.isEmpty(),
                notesViewModel.screenState.value.searchText,
                notesViewModel::changeSearchText
            )
            ActionBar(actionBarHeight, offsetX, notesViewModel)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(35.dp)
        ) {
            AnimatedVisibility(
                notesViewModel.screenState.value.selectedNotes.isEmpty(),
                enter = fadeIn(tween(200, easing = LinearOutSlowInEasing)),
                exit = fadeOut(tween(200, easing = FastOutSlowInEasing)),
            ) {
                FloatingActionButton(isActive = notesViewModel.screenState.value.selectedNotes.isEmpty()) {
                    notesViewModel.createNewNote()
                }
            }
        }
    }

    ReminderCheck(notesViewModel)
}

@Composable
private fun ReminderCheck(notesViewModel: NotesViewModel) {
    val context = LocalContext.current

    var reminderNeededDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var clickedOnEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    if (reminderNeededDialog)
        ActionDialog(
            dialogProperties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            icon = if (clickedOnEnabled) Icons.Outlined.CheckCircleOutline else Icons.Outlined.NotificationImportant,
            title = if (clickedOnEnabled) stringResource(id = R.string.ImportantForRemindersWork) else stringResource(id = R.string.NeededPermissionForNotifications),
            confirmText = if (clickedOnEnabled) stringResource(id = R.string.Confirm) else stringResource(id = R.string.Enable),
            onConfirm = {
                clickedOnEnabled = true
                if (!areNotificationsEnabled(context = context) || !areChanelNotificationsEnabled(
                        context = context,
                        channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                    )
                ) {
                    IntentHelper().startIntentAppNotificationSettings(
                        context = context,
                        channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                    )
                }
                if (!areEXACTNotificationsEnabled(context = context)) {
                    IntentHelper().startIntentAppEXACTNotificationSettings(context)
                }
                if (checkIsNotificationEnabled(context = context)) {
                    reminderNeededDialog = false
                }
            },
            goBackText = stringResource(id = R.string.Cancel),
            onGoBack = {
                reminderNeededDialog = false
            }
        )

    LaunchedEffect(notesViewModel.screenState.value.reminders.size, reminderNeededDialog) {
        if (notesViewModel.screenState.value.reminders.size > 0 && !checkIsNotificationEnabled(context = context))
            reminderNeededDialog = true
    }
}

private fun checkIsNotificationEnabled(context: Context) = areNotificationsEnabled(context = context) &&
        areEXACTNotificationsEnabled(context = context) &&
        areChanelNotificationsEnabled(
            context = context,
            channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
        )

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
                NoteCard(item.header,
                    item.body,
                    reminder = notesViewModel.screenState.value.reminders.find { it.noteId == item._id },
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
                NoteCard(item.header,
                    item.body,
                    reminder = notesViewModel.screenState.value.reminders.find { it.noteId == item._id },
                    markedText = notesViewModel.screenState.value.searchText,
                    selected = item._id in notesViewModel.screenState.value.selectedNotes,
                    onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
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
private fun ActionBar(
    actionBarHeight: Dp, offsetX: Animatable<Float, AnimationVector1D>, notesViewModel: NotesViewModel
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (notesViewModel.screenState.value.selectedNotes.isNotEmpty()) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground,
        animationSpec = tween(200),
    )
    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    val topAppBarElevation = if (offsetX.value.roundToInt() < -actionBarHeight.value.roundToInt()) 0.dp else 2.dp
    Box(
        modifier = Modifier
            .height(actionBarHeight)
            .offset { IntOffset(x = 0, y = offsetX.value.roundToInt()) },
    ) {
        CustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            elevation = topAppBarElevation,
            backgroundColor = CustomTheme.colors.secondaryBackground,
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
                    icon = Icons.Outlined.Archive,
                    iconDescription = R.string.AddToArchive,
                    onClick = notesViewModel::archiveSelectedNotes
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

