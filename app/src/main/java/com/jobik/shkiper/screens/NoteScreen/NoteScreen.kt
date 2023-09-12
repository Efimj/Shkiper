package com.jobik.shkiper.screens.NoteScreen

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.DropDownButton
import com.jobik.shkiper.ui.components.buttons.DropDownButtonSizeMode
import com.jobik.shkiper.ui.components.buttons.DropDownItem
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.ui.components.layouts.LinkPreviewList
import com.jobik.shkiper.ui.components.fields.CustomTextField
import com.jobik.shkiper.ui.components.fields.HashtagEditor
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomAppTheme
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    LaunchedEffect(noteViewModel.screenState.value.isGoBack) {
        noteViewModel.runFetchingLinksMetaData()
        if (noteViewModel.screenState.value.isGoBack) navController.popBackStack()
    }
    val scrollState = rememberLazyListState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    LaunchedEffect(currentRoute) {
        if (currentRoute.substringBefore("/") != AppScreens.Note.route.substringBefore("/")) {
            noteViewModel.setTopAppBarHover(false)
            noteViewModel.setBottomAppBarHover(false)
        }
    }
    val bodyFieldFocusRequester = remember { FocusRequester() }
    val linkListExpanded = remember { mutableStateOf(false) }

    Scaffold(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        topBar = { NoteScreenHeader(navController, noteViewModel) },
        bottomBar = { NoteScreenFooter(navController, noteViewModel) },
        modifier = Modifier.imePadding().navigationBarsPadding().fillMaxSize(),
    ) { contentPadding ->
        Box(Modifier.fillMaxSize().padding(contentPadding)) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() } // This is mandatory
                    ) {
                        bodyFieldFocusRequester.requestFocus()
                    }

            ) {
                val enabled = noteViewModel.screenState.value.notePosition != NotePosition.DELETE
                item {
                    CustomTextField(
                        text = noteViewModel.screenState.value.noteHeader,
                        onTextChange = { noteViewModel.updateNoteHeader(it) },
                        placeholder = stringResource(R.string.Header),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onAny = {
                                bodyFieldFocusRequester.requestFocus()
                            }
                        ),
                        enabled = enabled,
                        textStyle = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 21.sp
                        ),
                        modifier = Modifier.testTag("note_header_input").fillMaxSize()
                            .padding(bottom = 6.dp, top = 4.dp).padding(horizontal = 20.dp)
                    )
                }
                item {
                    CustomTextField(
                        text = noteViewModel.screenState.value.noteBody,
                        onTextChange = { noteViewModel.updateNoteBody(it) },
                        placeholder = stringResource(R.string.Text),
                        textStyle = MaterialTheme.typography.body1,
                        enabled = enabled,
                        modifier = Modifier.testTag("note_body_input").fillMaxWidth().padding(bottom = 10.dp)
                            .padding(horizontal = 20.dp)
                            .focusRequester(bodyFieldFocusRequester)
                    )
                }
                item {
                    HashtagEditor(
                        enabled = enabled,
                        modifier = Modifier.padding(bottom = 15.dp).padding(horizontal = 20.dp),
                        tags = noteViewModel.screenState.value.hashtags,
                        forSelectionTags = noteViewModel.screenState.value.allHashtags,
                        onSave = noteViewModel::changeNoteHashtags
                    )
                }
                LinkPreviewList(
                    noteViewModel.screenState.value.linksMetaData,
                    linkListExpanded,
                    noteViewModel.screenState.value.linksLoading,
                    Modifier.padding(horizontal = 20.dp)
                )
                item {
                    Spacer(Modifier.height(45.dp))
                }
            }
            if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE && noteViewModel.screenState.value.deletionDate != null)
                Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp)) {
                    SnackbarCard(
                        SnackbarVisualsCustom(
                            message = "${stringResource(R.string.DaysBeforeDeletingNote)} ${
                                7 - Duration.between(
                                    noteViewModel.screenState.value.deletionDate,
                                    LocalDateTime.now()
                                ).toDays()
                            }",
                            icon = Icons.Outlined.Warning
                        )
                    )
                }
        }
        if (noteViewModel.screenState.value.isDeleteDialogShow)
            ActionDialog(
                title = stringResource(R.string.DeleteForever),
                icon = Icons.Outlined.Warning,
                confirmText = stringResource(R.string.Confirm),
                onConfirm = noteViewModel::deleteNote,
                goBackText = stringResource(R.string.Cancel),
                onGoBack = noteViewModel::switchDeleteDialogShow
            )
    }

    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
        val reminder = remember { noteViewModel.screenState.value.reminder }
        val reminderDialogProperties = remember {
            if (reminder != null) ReminderDialogProperties(reminder.date, reminder.time, reminder.repeat)
            else ReminderDialogProperties()
        }
        CreateReminderDialog(
            reminderDialogProperties = reminderDialogProperties,
            onGoBack = noteViewModel::switchReminderDialogShow,
            onDelete = if (reminder != null) noteViewModel::deleteReminder else null,
            onSave = noteViewModel::createReminder,
        )
    }

    LaunchedEffect(scrollState.canScrollBackward, scrollState.canScrollForward) {
        if (scrollState.canScrollBackward || scrollState.canScrollForward) {
            noteViewModel.setTopAppBarHover(scrollState.canScrollBackward)
            noteViewModel.setBottomAppBarHover(scrollState.canScrollForward)
        } else {
            noteViewModel.setTopAppBarHover(false)
            noteViewModel.setBottomAppBarHover(false)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            noteViewModel.deleteNoteIfEmpty()
        }
    }
}

@Composable
private fun NoteScreenHeader(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.screenState.value.isTopAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    CustomTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        elevation = if (noteViewModel.screenState.value.isTopAppBarHover) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        navigation = TopAppBarItem(
            icon = Icons.Default.ArrowBack,
            iconDescription = R.string.GoBack,
            modifier = Modifier.testTag("button_navigate_back"),
            onClick = navController::popBackStack
        ),
        items =
        if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE)
            listOf(
                TopAppBarItem(
                    icon = Icons.Outlined.History,
                    iconDescription = R.string.Restore,
                    onClick = noteViewModel::removeNoteFromBasket
                )
            )
        else
            listOf(
                TopAppBarItem(
                    isActive = noteViewModel.screenState.value.isPinned,
                    icon = Icons.Outlined.PushPin,
                    iconDescription = R.string.AttachNote,
                    onClick = noteViewModel::switchNotePinnedMode
                ),
                TopAppBarItem(
                    isActive = noteViewModel.screenState.value.reminder != null,
                    icon = Icons.Outlined.NotificationAdd,
                    iconDescription = R.string.AddToNotification,
                    onClick = noteViewModel::switchReminderDialogShow
                ),
                TopAppBarItem(
                    isActive = noteViewModel.screenState.value.notePosition == NotePosition.ARCHIVE,
                    icon = if (noteViewModel.screenState.value.notePosition == NotePosition.ARCHIVE) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
                    iconDescription = if (noteViewModel.screenState.value.notePosition == NotePosition.ARCHIVE) R.string.UnarchiveNotes else R.string.AddToArchive,
                    onClick = if (noteViewModel.screenState.value.notePosition == NotePosition.ARCHIVE) noteViewModel::unarchiveNote else noteViewModel::archiveNote
                ),
            )
    )
}


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NoteScreenFooter(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.screenState.value.isBottomAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    BottomAppBar(
        elevation = if (noteViewModel.screenState.value.isBottomAppBarHover) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        contentColor = CustomAppTheme.colors.textSecondary,
        cutoutShape = CircleShape,
        modifier = Modifier.fillMaxWidth().height(50.dp),
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (noteViewModel.screenState.value.intermediateStates.size < 2) {
                Row {
                    Spacer(modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp))
                    Text(
                        "${stringResource(R.string.ChangedAt)} ${getUpdatedTime(noteViewModel)}",
                        modifier = Modifier.basicMarquee(),
                        style = MaterialTheme.typography.body1.copy(fontSize = 15.sp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.padding(45.dp, 0.dp, 0.dp, 0.dp))
                Row {
                    IconButton(
                        onClick = { noteViewModel.noteStateGoBack() },
                        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                        enabled = noteViewModel.screenState.value.currentIntermediateIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Undo,
                            contentDescription = stringResource(R.string.GoBack),
                            tint = if (noteViewModel.screenState.value.currentIntermediateIndex > 0) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                        )
                    }
                    Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                    IconButton(
                        onClick = { noteViewModel.noteStateGoNext() },
                        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                        enabled = noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Redo,
                            contentDescription = stringResource(R.string.GoForward),
                            tint = if (noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                        )
                    }
                }
            }
            Row {
                val dropDownItems =
                    if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE)
                        listOf(
                            DropDownItem(
                                text = stringResource(R.string.Delete),
                                icon = Icons.Outlined.DeleteForever
                            )
                        ) else
                        listOf(
                            DropDownItem(
                                text = stringResource(R.string.ShareNote),
                                icon = Icons.Outlined.Share
                            ),
                            DropDownItem(
                                text = stringResource(R.string.CreateWidget),
                                icon = Icons.Outlined.Widgets
                            ),
                            DropDownItem(
                                text = stringResource(R.string.Delete),
                                icon = if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE) Icons.Outlined.DeleteForever else Icons.Outlined.Delete
                            )
                        )

                val context = LocalContext.current
                DropDownButton(
                    items = dropDownItems,
                    selectedIndex = 0,
                    modifier = Modifier,
                    stretchMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
                    onChangedSelection = { index ->
                        if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE)
                            when (index) {
                                0 -> noteViewModel.switchDeleteDialogShow()
                            }
                        else
                            when (index) {
                                0 -> noteViewModel.shareNoteText(context)
                                1 -> noteViewModel.createWidget()
                                2 -> {
                                    if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE)
                                        noteViewModel.deleteNote()
                                    else
                                        noteViewModel.moveToBasket()
                                }
                            }
                    }
                ) {
                    IconButton(
                        onClick = { it() },
                        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = stringResource(R.string.AddToBasket),
                            tint = CustomAppTheme.colors.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            }
        }
    }
}


private fun getUpdatedTime(noteViewModel: NoteViewModel): String {
    val duration = Duration.between(noteViewModel.screenState.value.updatedDate, LocalDateTime.now())
    return if (duration.toDays() > 0)
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm"))
    else
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("HH:mm"))
}