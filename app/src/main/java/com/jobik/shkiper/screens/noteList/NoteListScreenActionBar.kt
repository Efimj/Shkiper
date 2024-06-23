package com.jobik.shkiper.screens.noteList

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.runtime.Composable
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem

@Composable
fun NoteListScreenActionBar(
    isVisible: Boolean,
    notesViewModel: NotesViewModel
) {
    val items = when (notesViewModel.screenState.value.currentNotes) {
        NotePosition.MAIN -> listOf(
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

        NotePosition.ARCHIVE -> listOf(
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
                icon = Icons.Outlined.Unarchive,
                iconDescription = R.string.AddToArchive,
                onClick = notesViewModel::unarchiveSelectedNotes
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.Delete,
                iconDescription = R.string.AddToBasket,
                onClick = notesViewModel::moveSelectedNotesToBasket
            ),
        )

        NotePosition.DELETE -> listOf(
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.History,
                iconDescription = R.string.Restore,
                onClick = notesViewModel::removeSelectedNotesFromBasket
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.DeleteForever,
                iconDescription = R.string.Delete,
                onClick = notesViewModel::switchDeleteDialogShow
            ),
        )
    }

    CustomTopAppBar(
        isVisible = isVisible,
        counter = notesViewModel.screenState.value.selectedNotes.count(),
        navigation = TopAppBarItem(
            isActive = false,
            icon = Icons.Default.Close,
            iconDescription = R.string.GoBack,
            onClick = notesViewModel::clearSelectedNote
        ),
        items = items
    )
}