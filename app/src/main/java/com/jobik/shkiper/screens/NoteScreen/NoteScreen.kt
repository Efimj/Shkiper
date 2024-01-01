package com.jobik.shkiper.screens.NoteScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.buttons.DropDownButton
import com.jobik.shkiper.ui.components.buttons.DropDownButtonSizeMode
import com.jobik.shkiper.ui.components.buttons.DropDownItem
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.ui.components.fields.CustomRichTextEditor
import com.jobik.shkiper.ui.components.fields.CustomDefaultTextField
import com.jobik.shkiper.ui.components.fields.HashtagEditor
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalRichTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(noteViewModel.screenState.value.isGoBack) {
        noteViewModel.runFetchingLinksMetaData()
        if (noteViewModel.screenState.value.isGoBack) navController.popBackStack()
    }
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    LaunchedEffect(currentRoute) {
        if (currentRoute.substringBefore("/") != AppScreens.Note.route.substringBefore("/")) {
            noteViewModel.setTopAppBarHover(false)
            noteViewModel.setBottomAppBarHover(false)
        }
    }
    val bodyFieldFocusRequester = remember { FocusRequester() }
    val linkListExpanded = remember { mutableStateOf(false) }

    val codeColor = CustomTheme.colors.textOnActive
    val codeBackgroundColor = CustomTheme.colors.active.copy(alpha = .2f)
    val codeStrokeColor = CustomTheme.colors.active
    val linkColor = CustomTheme.colors.text

    val richTextState = rememberRichTextState()

    LaunchedEffect(Unit) {
        richTextState.setConfig(
            linkColor = linkColor,
            linkTextDecoration = TextDecoration.Underline,
            codeColor = codeColor,
            codeBackgroundColor = codeBackgroundColor,
            codeStrokeColor = codeStrokeColor
        )
        richTextState.setHtml(noteViewModel.screenState.value.noteBody)
    }

    LaunchedEffect(richTextState.annotatedString) {
        if (noteViewModel.screenState.value.noteBody !== richTextState.toMarkdown())
            noteViewModel.updateNoteBody(richTextState.toHtml())
    }

    /**
     * When user styling a note
     */
    BackHandler(
        enabled = noteViewModel.screenState.value.isStyling, onBack = noteViewModel::switchStyling
    )

    Scaffold(
        backgroundColor = CustomTheme.colors.mainBackground,
        topBar = { NoteScreenHeader(navController, noteViewModel, richTextState) },
        bottomBar = { NoteScreenFooter(navController, noteViewModel, richTextState) },
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
            .fillMaxSize(),
    ) { contentPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() } // This is mandatory
                    ) {
                        bodyFieldFocusRequester.requestFocus()
                    }

            ) {
                val enabled = noteViewModel.screenState.value.notePosition != NotePosition.DELETE
                item {
                    CustomDefaultTextField(
                        text = noteViewModel.screenState.value.noteHeader,
                        onTextChange = { noteViewModel.updateNoteHeader(it) },
                        placeholder = stringResource(R.string.Header),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = true
                        ),
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
                        modifier = Modifier
                            .testTag("note_header_input")
                            .fillMaxSize()
                            .padding(bottom = 6.dp, top = 4.dp)
                            .padding(horizontal = 20.dp)
                    )
                }
                item {
                    CustomRichTextEditor(
                        state = richTextState,
                        placeholder = stringResource(R.string.Text),
                        textStyle = MaterialTheme.typography.body1,
                        enabled = enabled,
                        modifier = Modifier
                            .testTag("note_body_input")
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .padding(horizontal = 20.dp)
                            .focusRequester(bodyFieldFocusRequester)
                            .onFocusChanged { state ->
                                if (state.isFocused) {
                                    noteViewModel.switchStylingEnabled(true)
                                } else {
                                    noteViewModel.switchStyling(false)
                                    noteViewModel.switchStylingEnabled(false)
                                }
                            }
                    )
                }
                item {
                    HashtagEditor(
                        enabled = enabled,
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .padding(horizontal = 20.dp),
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
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp)
                ) {
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

    LaunchedEffect(noteViewModel.screenState.value.showShareDialog) {
        if (!noteViewModel.screenState.value.showShareDialog) {
            shareSheetState.hide()
        }
    }

    if (noteViewModel.screenState.value.showShareDialog) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchShowShareDialog(mode = false)
            },
            dragHandle = null,
        ) {
            SettingsItem(
                icon = Icons.Outlined.ContentCopy,
                title = "Share text",
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        richTextState.annotatedString.text
                    );noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                icon = Icons.Outlined.Html,
                title = "Share HTML text",
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        richTextState.toHtml()
                    );noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                icon = Icons.Outlined.Code,
                title = "Share markdown text",
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        richTextState.toMarkdown()
                    );noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                modifier = Modifier.heightIn(min = 50.dp),
                icon = Icons.Outlined.Screenshot,
                title = "Share image",
                onClick = { })
        }
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

    val secondaryBackgroundColor = CustomTheme.colors.secondaryBackground
    DisposableEffect(Unit) {
        onDispose {
            noteViewModel.deleteNoteIfEmpty(richTextState.annotatedString.toString())
            systemUiController.setNavigationBarColor(secondaryBackgroundColor)
        }
    }
}

@Composable
private fun NoteScreenHeader(navController: NavController, noteViewModel: NoteViewModel, richTextState: RichTextState) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.screenState.value.isTopAppBarHover) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground,
        animationSpec = tween(200),
    )
    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = if (noteViewModel.screenState.value.isTopAppBarHover) 8.dp else 0.dp
    ) {
        AnimateVerticalSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            directionUp = false,
            state = noteViewModel.screenState.value.isStyling,
            topComponent = {
                CustomTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 0.dp,
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
            },
            bottomComponent = {
                RichTextHeaderToolBar(state = richTextState)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NoteScreenFooter(navController: NavController, noteViewModel: NoteViewModel, richTextState: RichTextState) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.screenState.value.isBottomAppBarHover) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = if (noteViewModel.screenState.value.isTopAppBarHover) 8.dp else 0.dp
    ) {
        AnimateVerticalSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            directionUp = true,
            state = noteViewModel.screenState.value.isStyling,
            topComponent = {
                BottomAppBar(
                    elevation = if (noteViewModel.screenState.value.isBottomAppBarHover) 8.dp else 0.dp,
                    backgroundColor = backgroundColor,
                    contentColor = CustomTheme.colors.textSecondary,
                    cutoutShape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            IconButton(
                                onClick = { noteViewModel.switchStyling() },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .padding(0.dp),
                                enabled = noteViewModel.screenState.value.isStylingEnabled
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.TextFormat,
                                    contentDescription = "",
                                    tint = if (noteViewModel.screenState.value.isStyling && noteViewModel.screenState.value.isStylingEnabled) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                                )
                            }
                        }
                        if (noteViewModel.screenState.value.intermediateStates.size < 2) {
                            Text(
                                "${stringResource(R.string.ChangedAt)} ${getUpdatedTime(noteViewModel)}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                                    .basicMarquee(),
                                style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                            )
                        } else {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                            ) {
                                IconButton(
                                    onClick = { noteViewModel.noteStateGoBack() },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .padding(0.dp),
                                    enabled = noteViewModel.screenState.value.currentIntermediateIndex > 0
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24),
                                        contentDescription = stringResource(R.string.GoBack),
                                        tint = if (noteViewModel.screenState.value.currentIntermediateIndex > 0) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                                    )
                                }
                                IconButton(
                                    onClick = { noteViewModel.noteStateGoNext() },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .padding(0.dp),
                                    enabled = noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.redo_fill0_wght400_grad0_opsz24),
                                        contentDescription = stringResource(R.string.GoForward),
                                        tint = if (noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
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
                            val isExpanded = remember { mutableStateOf(false) }
                            DropDownButton(
                                items = dropDownItems,
                                selectedIndex = 0,
                                expanded = isExpanded,
                                modifier = Modifier,
                                stretchMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
                                onChangedSelection = { index ->
                                    if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE)
                                        when (index) {
                                            0 -> noteViewModel.switchDeleteDialogShow()
                                        }
                                    else
                                        when (index) {
                                            0 -> noteViewModel.switchShowShareDialog()
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
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .padding(0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = stringResource(R.string.AddToBasket),
                                        tint = CustomTheme.colors.textSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomComponent = {
                RichTextBottomToolBar(state = richTextState, onClose = { noteViewModel.switchStyling(false) })
            }
        )
    }
}


private fun getUpdatedTime(noteViewModel: NoteViewModel): String {
    val duration = Duration.between(noteViewModel.screenState.value.updatedDate, LocalDateTime.now())
    return if (duration.toDays() > 0)
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm"))
    else
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("HH:mm"))
}