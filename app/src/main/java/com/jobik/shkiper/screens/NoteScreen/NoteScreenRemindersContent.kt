package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet

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

        }
    }
}