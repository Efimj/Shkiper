package com.jobik.shkiper.screens.noteListScreen.NoteListScreenContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun NoteListScreenActionBar(
    isVisible: Boolean,
    notesViewModel: NotesViewModel
) {
    CustomTopAppBar(
        isVisible = isVisible,
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