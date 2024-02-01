package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.ReminderCard
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState()

    LaunchedEffect(noteViewModel.screenState.value.isCreateReminderDialogShow) {
        if (!noteViewModel.screenState.value.isCreateReminderDialogShow) {
            shareSheetState.hide()
        }
    }

    shareSheetState.currentValue

    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchReminderDialogShow()
            },
            dragHandle = null,
        ) {
            Box {
                Column {

                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(items = noteViewModel.screenState.value.reminders) { item ->
                            ReminderCard(reminder = item) {

                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 10.dp)
                ) {
                    BottomBar()
                }
            }
        }
    }
}

@Composable
private fun BottomBar() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = CustomTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            contentColor = CustomTheme.colors.textOnActive,
            containerColor = CustomTheme.colors.active
        ),
        border = null,
        elevation = null,
        contentPadding = PaddingValues(horizontal = 15.dp),
        onClick = {

        }
    ) {
        Text(
            text = stringResource(R.string.CreateReminder),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.SemiBold,
            color = CustomTheme.colors.textOnActive,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CreateReminderDialog(noteViewModel: NoteViewModel) {
//    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
//        val reminder = remember { noteViewModel.screenState.value.reminders }
//        val reminderDialogProperties = remember {
//            if (reminder != null) ReminderDialogProperties(reminder.date, reminder.time, reminder.repeat)
//            else ReminderDialogProperties()
//        }
//        com.jobik.shkiper.ui.components.modals.CreateReminderDialog(
//            reminderDialogProperties = reminderDialogProperties,
//            onGoBack = noteViewModel::switchReminderDialogShow,
//            onDelete = if (reminder != null) noteViewModel::deleteReminder else null,
//            onSave = noteViewModel::createReminder,
//        )
//    }
}