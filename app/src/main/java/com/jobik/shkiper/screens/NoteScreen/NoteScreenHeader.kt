package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    val backgroundColorValue =
        if (noteViewModel.screenState.value.isTopAppBarHover || noteViewModel.screenState.value.isStyling) CustomTheme.colors.secondaryContainer else CustomTheme.colors.background

    val backgroundColor by animateColorAsState(
        backgroundColorValue, label = "backgroundColor",
    )

    val contentColorValue =
        if (noteViewModel.screenState.value.isTopAppBarHover || noteViewModel.screenState.value.isStyling) CustomTheme.colors.onSecondaryContainer else CustomTheme.colors.textSecondary

    val contentColor by animateColorAsState(
        contentColorValue, label = "contentColor",
    )

    val shadowElevation = animateDpAsState(
        targetValue = if (noteViewModel.screenState.value.isTopAppBarHover) 8.dp else 0.dp,
        label = "shadowElevation"
    )

    val localDensity = LocalDensity.current
    var barHeight by remember { mutableStateOf(54.dp) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = shadowElevation.value
    ) {
        AnimateVerticalSwitch(
            modifier = Modifier,
            directionUp = false,
            state = noteViewModel.screenState.value.isStyling,
            topComponent = {
                CustomTopAppBar(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        // Set screen height using the LayoutCoordinates
                        barHeight = with(localDensity) { coordinates.size.height.toDp() }
                    },
                    backgroundColor = Color.Transparent,
                    contentColor = contentColor,
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
                                isActive = noteViewModel.screenState.value.reminders.isNotEmpty(),
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
                Box(modifier = Modifier.height(barHeight), contentAlignment = Alignment.Center) {
                    RichTextHeaderToolBar(state = richTextState)
                }
            }
        )
    }
}