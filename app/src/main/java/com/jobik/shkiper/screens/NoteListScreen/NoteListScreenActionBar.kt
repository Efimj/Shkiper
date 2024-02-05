package com.jobik.shkiper.screens.NoteListScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.viewmodels.NotesViewModel
import kotlin.math.roundToInt

@Composable
fun NoteListScreenActionBar(
    actionBarHeight: Dp, offsetX: Animatable<Float, AnimationVector1D>, notesViewModel: NotesViewModel
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColorValue =
        if (notesViewModel.screenState.value.selectedNotes.isNotEmpty()) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground

    LaunchedEffect(notesViewModel.screenState.value.selectedNotes.isNotEmpty()) {
        systemUiController.setStatusBarColor(backgroundColorValue)
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
            counter = notesViewModel.screenState.value.selectedNotes.count(),
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