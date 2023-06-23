package com.example.notepadapp.screens.NoteScreen

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.AppScreens
import com.example.notepadapp.ui.components.fields.CustomTextField
import com.example.notepadapp.ui.components.modals.CreateReminderDialog
import com.example.notepadapp.ui.components.modals.ReminderDialogProperties
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        if (noteViewModel.note == null) navController.popBackStack()
    }
    val scrollState = rememberScrollState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    LaunchedEffect(currentRoute) {
        if (currentRoute.substringBefore("/") != AppScreens.Note.route.substringBefore("/")) {
            noteViewModel.isTopAppBarHover = false
            noteViewModel.isBottomAppBarHover = false
        }
    }
    val bodyFieldFocusRequester = remember { FocusRequester() }

    Scaffold(
        backgroundColor = CustomAppTheme.colors.mainBackground,
        topBar = { NoteScreenHeader(navController, noteViewModel) },
        bottomBar = { NoteScreenFooter(navController, noteViewModel) },
        modifier = Modifier.imePadding().navigationBarsPadding().fillMaxSize(),
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
                    bodyFieldFocusRequester.requestFocus()
                }

        ) {
            CustomTextField(
                text = noteViewModel.noteHeader,
                onTextChange = { noteViewModel.updateNoteHeader(it) },
                placeholder = "Header",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onAny = {
                        bodyFieldFocusRequester.requestFocus()
                    }
                ),
                textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 21.sp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(bottom = 6.dp, top = 4.dp)
            )
            CustomTextField(
                text = noteViewModel.noteBody,
                onTextChange = { noteViewModel.updateNoteBody(it) },
                placeholder = "Text",
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                    .focusRequester(bodyFieldFocusRequester)
            )
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

    LaunchedEffect(scrollState.value) {
        if (scrollState.canScrollBackward || scrollState.canScrollForward) {
            noteViewModel.isTopAppBarHover = scrollState.value > 0
            noteViewModel.isBottomAppBarHover = scrollState.value < scrollState.maxValue
        } else {
            noteViewModel.isTopAppBarHover = false
            noteViewModel.isBottomAppBarHover = false
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
        if (noteViewModel.isTopAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )

    SideEffect {
        systemUiController.setStatusBarColor(backgroundColor)
    }

    TopAppBar(
        elevation = if (noteViewModel.isTopAppBarHover) 8.dp else 0.dp,
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
                    contentDescription = "Go back",
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
                    contentDescription = "Attach a note",
                    tint = if (noteViewModel.noteIsPinned) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { noteViewModel.switchReminderDialogShow() },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationAdd,
                    contentDescription = "Add to notification",
                    tint = if (noteViewModel.reminder.value == null) CustomAppTheme.colors.textSecondary else CustomAppTheme.colors.text,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Archive,
                    contentDescription = "Add to archive",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NoteScreenFooter(navController: NavController, noteViewModel: NoteViewModel) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor by animateColorAsState(
        if (noteViewModel.isBottomAppBarHover) CustomAppTheme.colors.secondaryBackground else CustomAppTheme.colors.mainBackground,
        animationSpec = tween(200),
    )
    val formatter = SimpleDateFormat("dd MM yyyy : ss", Locale.getDefault())

    SideEffect {
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    BottomAppBar(
        elevation = if (noteViewModel.isBottomAppBarHover) 8.dp else 0.dp,
        backgroundColor = backgroundColor,
        contentColor = CustomAppTheme.colors.textSecondary,
        cutoutShape = CircleShape,
        modifier = Modifier.fillMaxWidth().height(45.dp),
    ) {
        Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        Text(formatter.format(noteViewModel.noteUpdatedDate))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Add to basket",
                    tint = CustomAppTheme.colors.textSecondary,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
        }
    }
}