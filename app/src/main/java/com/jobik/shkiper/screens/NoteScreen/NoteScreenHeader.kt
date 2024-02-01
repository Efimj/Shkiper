package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.RichTextHeaderToolBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun NoteScreenHeader(navController: NavController, noteViewModel: NoteViewModel, richTextState: RichTextState) {
    val systemUiController = rememberSystemUiController()

    val backgroundColorValue =
        if (noteViewModel.screenState.value.isTopAppBarHover) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground

    val backgroundColor by animateColorAsState(
        backgroundColorValue, animationSpec = tween(200),
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