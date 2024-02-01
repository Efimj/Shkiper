package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.Box
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val context = LocalContext.current
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(noteViewModel.screenState.value.isCreateReminderDialogShow) {
        if (!noteViewModel.screenState.value.isCreateReminderDialogShow) {
            shareSheetState.hide()
        }
    }

    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchReminderDialogShow()
            },
            dragHandle = null,
        ) {
            Box {
                LazyColumn(){

                }

            }
        }
    }
}

@Composable
private fun CreateReminderDialog(noteViewModel: NoteViewModel) {
    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
        val reminder = remember { noteViewModel.screenState.value.reminder }
        val reminderDialogProperties = remember {
            if (reminder != null) ReminderDialogProperties(reminder.date, reminder.time, reminder.repeat)
            else ReminderDialogProperties()
        }
        com.jobik.shkiper.ui.components.modals.CreateReminderDialog(
            reminderDialogProperties = reminderDialogProperties,
            onGoBack = noteViewModel::switchReminderDialogShow,
            onDelete = if (reminder != null) noteViewModel::deleteReminder else null,
            onSave = noteViewModel::createReminder,
        )
    }
}