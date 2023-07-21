package com.android.notepad.screens.NoteScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.notepad.R
import com.android.notepad.database.models.RepeatMode
import com.android.notepad.navigation.AppScreens
import com.android.notepad.ui.components.buttons.DropDownButton
import com.android.notepad.ui.components.buttons.DropDownButtonSizeMode
import com.android.notepad.ui.components.buttons.DropDownItem
import com.android.notepad.ui.components.buttons.RoundedButton
import com.android.notepad.ui.components.layouts.LinkPreviewList
import com.android.notepad.ui.components.fields.CustomTextField
import com.android.notepad.ui.components.fields.HashtagEditor
import com.android.notepad.ui.components.modals.CreateReminderDialog
import com.android.notepad.ui.components.modals.ReminderDialogProperties
import com.android.notepad.ui.theme.CustomAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        noteViewModel.runFetchingLinksMetaData()
        if (noteViewModel.note == null) navController.popBackStack()
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
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize().padding(contentPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
                    bodyFieldFocusRequester.requestFocus()
                }

        ) {
            item {
                CustomTextField(
                    text = noteViewModel.noteHeader.value,
                    onTextChange = { noteViewModel.updateNoteHeader(it) },
                    placeholder = stringResource(R.string.Header),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onAny = {
                            bodyFieldFocusRequester.requestFocus()
                        }
                    ),
                    textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 21.sp),
                    modifier = Modifier.fillMaxSize().padding(bottom = 6.dp, top = 4.dp).padding(horizontal = 20.dp)
                )
            }
            item {
                CustomTextField(
                    text = noteViewModel.noteBody.value,
                    onTextChange = { noteViewModel.updateNoteBody(it) },
                    placeholder = stringResource(R.string.Text),
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).padding(horizontal = 20.dp)
                        .focusRequester(bodyFieldFocusRequester)
                )
            }
            item {
                HashtagEditor(
                    Modifier.padding(bottom = 15.dp).padding(horizontal = 20.dp),
                    noteViewModel.noteHashtags.value,
                    noteViewModel::changeNoteHashtags
                )
            }
            LinkPreviewList(
                noteViewModel.linksData.value,
                linkListExpanded,
                noteViewModel.linksLoading,
                Modifier.padding(horizontal = 20.dp)
            )
            item {
                Spacer(Modifier.height(45.dp))
            }
        }
    }

    if (noteViewModel.isCreateReminderDialogShow.value) {
        val reminder = remember { noteViewModel.reminder.value }
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
            noteViewModel.saveChanges()
            noteViewModel.deleteNoteIfEmpty()
        }
    }
}

@Composable
private fun NoteScreenHeader(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.isTopAppBarHover.value) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    TopAppBar(
        elevation = if (noteViewModel.isTopAppBarHover.value) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        contentColor = CustomAppTheme.colors.textSecondary,
        title = { },
        navigationIcon = {
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.GoBack),
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
        },
        actions = {
            IconButton(
                onClick = { noteViewModel.switchNotePinnedMode() },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = stringResource(R.string.AttachNote),
                    tint = if (noteViewModel.noteIsPinned.value) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { noteViewModel.switchReminderDialogShow() },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationAdd,
                    contentDescription = stringResource(R.string.AddToNotification),
                    tint = if (noteViewModel.reminder.value == null) CustomAppTheme.colors.textSecondary else CustomAppTheme.colors.text,
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
        },
        modifier = Modifier.fillMaxWidth(),
    )
}


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NoteScreenFooter(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.isBottomAppBarHover.value) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    BottomAppBar(
        elevation = if (noteViewModel.isBottomAppBarHover.value) 8.dp else 0.dp,
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
            if (noteViewModel.intermediateStates.value.size < 2) {
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
                        enabled = noteViewModel.currentStateIndex.value > 0
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Undo,
                            contentDescription = stringResource(R.string.GoBack),
                            tint = if (noteViewModel.currentStateIndex.value > 0) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                        )
                    }
                    Spacer(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                    IconButton(
                        onClick = { noteViewModel.noteStateGoNext() },
                        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                        enabled = noteViewModel.currentStateIndex.value < noteViewModel.intermediateStates.value.size - 1
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Redo,
                            contentDescription = stringResource(R.string.GoForward),
                            tint = if (noteViewModel.currentStateIndex.value < noteViewModel.intermediateStates.value.size - 1) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                        )
                    }
                }
            }
            Row {
                val dropDownItems = listOf(
                    DropDownItem(
                        text = stringResource(R.string.ShareNote),
                        icon = Icons.Outlined.Share
                    ),
                    DropDownItem(
                        text = stringResource(R.string.Delete),
                        icon = Icons.Outlined.Delete
                    )
                )

                DropDownButton(
                    items = dropDownItems,
                    selectedIndex = 0,
                    modifier = Modifier,
                    stretchMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
                    onChangedSelection = { index ->
                        when (index) {
                            0 -> noteViewModel.shareNoteText()
                            1 -> noteViewModel.moveToBasket()
                        }
                    }
                ) {
                    IconButton(
                        onClick = { it() },
                        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
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
    val duration = Duration.between(noteViewModel.noteUpdatedDate.value, LocalDateTime.now())
    return if (duration.toDays() > 0)
        noteViewModel.noteUpdatedDate.value.format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm"))
    else
        noteViewModel.noteUpdatedDate.value.format(DateTimeFormatter.ofPattern("HH:mm"))
}