package com.jobik.shkiper.screens.NoteScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.ui.components.fields.CustomDefaultTextField
import com.jobik.shkiper.ui.components.fields.CustomRichTextEditor
import com.jobik.shkiper.ui.components.fields.TagEditor
import com.jobik.shkiper.ui.components.layouts.LinkPreviewList
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.SetRichTextDefaultStyles
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun NoteScreenContent(
    noteViewModel: NoteViewModel,
    navController: NavController
) {
    RemoveIndicatorWhenKeyboardHidden(noteViewModel)
    val richTextState = rememberRichTextState()
    val bodyFieldFocusRequester = remember { FocusRequester() }
    val scrollState = rememberLazyListState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val linkListExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        if (currentRoute.substringBefore("/") != Route.Note.route.substringBefore("/")) {
            noteViewModel.setTopAppBarHover(false)
            noteViewModel.setBottomAppBarHover(false)
        }
    }

    SetRichTextDefaultStyles(richTextState)
    LaunchedEffect(Unit) {
        richTextState.setHtml(noteViewModel.screenState.value.noteBody)
    }
    LaunchedEffect(richTextState.annotatedString) {
        if (noteViewModel.screenState.value.noteBody != richTextState.toHtml())
            noteViewModel.updateNoteBody(richTextState.toHtml())
    }
    BackHandlerWithStylingState(noteViewModel)

    Scaffold(
        containerColor = CustomTheme.colors.mainBackground,
        topBar = { NoteScreenHeader(navController, noteViewModel, richTextState) },
        bottomBar = { NoteScreenFooter(navController, noteViewModel, richTextState) },
        contentWindowInsets = WindowInsets.ime,
        modifier = Modifier.fillMaxSize().imePadding(),
    ) { contentPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .horizontalWindowInsetsPadding()
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
                        minLines = 2,
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
                    TagEditor(
                        enabled = enabled,
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .padding(horizontal = 20.dp),
                        selectedTags = noteViewModel.screenState.value.hashtags,
                        allTags = noteViewModel.screenState.value.allHashtags,
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
            SnackbarWhenNoteDeleted(noteViewModel)
        }
    }

    SetFocusOnNoteBodyIfNoteNew(noteViewModel, richTextState, bodyFieldFocusRequester)
    NoteScreenShareComponent(noteViewModel, richTextState)
    DeleteonDialog(noteViewModel)
    AndroidBarColorManager(scrollState, noteViewModel)
    CheckAndDeleteNoteOnExit(noteViewModel, richTextState)
    HideKeyboardWhenLeaveScreen()
}

@Composable
private fun SetFocusOnNoteBodyIfNoteNew(
    noteViewModel: NoteViewModel,
    richTextState: RichTextState,
    bodyFieldFocusRequester: FocusRequester
) {
    val isInitialized = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        if (isInitialized.value) return@LaunchedEffect
        if (noteViewModel.screenState.value.noteHeader.isBlank() && richTextState.annotatedString.toString()
                .isBlank()
        ) {
            bodyFieldFocusRequester.requestFocus()
        }
    }
}

@Composable
private fun BackHandlerWithStylingState(noteViewModel: NoteViewModel) {
    /**
     * When user styling a note
     */
    BackHandler(
        enabled = noteViewModel.screenState.value.isStyling, onBack = noteViewModel::switchStyling
    )
}

@Composable
private fun AndroidBarColorManager(
    scrollState: LazyListState,
    noteViewModel: NoteViewModel
) {
    LaunchedEffect(scrollState.canScrollBackward, scrollState.canScrollForward) {
        if (scrollState.canScrollBackward || scrollState.canScrollForward) {
            noteViewModel.setTopAppBarHover(scrollState.canScrollBackward)
            noteViewModel.setBottomAppBarHover(scrollState.canScrollForward)
        } else {
            noteViewModel.setTopAppBarHover(false)
            noteViewModel.setBottomAppBarHover(false)
        }
    }
}

@Composable
private fun DeleteonDialog(noteViewModel: NoteViewModel) {
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

@Composable
private fun BoxScope.SnackbarWhenNoteDeleted(noteViewModel: NoteViewModel) {
    if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE && noteViewModel.screenState.value.deletionDate != null)
        Box(
            modifier = Modifier.Companion
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

@Composable
private fun CheckAndDeleteNoteOnExit(
    noteViewModel: NoteViewModel,
    richTextState: RichTextState
) {
    DisposableEffect(Unit) {
        onDispose {
            noteViewModel.deleteNoteIfEmpty(richTextState.annotatedString.toString())
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun HideKeyboardWhenLeaveScreen() {
    val keyboardController = LocalSoftwareKeyboardController.current
    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }
}

@Composable
private fun RemoveIndicatorWhenKeyboardHidden(noteViewModel: NoteViewModel) {
    val isKeyboardVisible by keyboardAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isKeyboardVisible, noteViewModel.screenState.value.isStyling) {
        // for cases of inserting a link through a window
        if (noteViewModel.screenState.value.isStyling) return@LaunchedEffect
        if (isKeyboardVisible == Keyboard.Closed) {
            focusManager.clearFocus()
        }
    }
}