package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.ui.components.cards.ReminderCard
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState()
    var currentReminder by rememberSaveable { mutableStateOf<Reminder?>(null) }
    val openCreateReminderDialog = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(noteViewModel.screenState.value.isReminderMenuNeeded) {
        if (!noteViewModel.screenState.value.isReminderMenuNeeded) {
            currentReminder = null
            openCreateReminderDialog.value = false
            shareSheetState.hide()
        } else {
            if (noteViewModel.screenState.value.reminders.isEmpty()) {
                openCreateReminderDialog.value = true
            }
        }
    }

    if (noteViewModel.screenState.value.isReminderMenuNeeded) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchReminderDialogShow()
            },
            dragHandle = null,
        ) {
            Box {
                Column {
                    Header()
                    if (noteViewModel.screenState.value.reminders.isEmpty()) {
                        ScreenContentIfNoData(modifier = Modifier.fillMaxHeight(.3f), title = R.string.Reminders, icon = Icons.Outlined.NotificationsOff)
                    } else {
                        LazyColumn(
                            modifier = Modifier,
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(items = noteViewModel.screenState.value.reminders) { item ->
                                ReminderCard(reminder = item) {
                                    currentReminder = item
                                    openCreateReminderDialog.value = true
                                }
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
                    BottomBar() {
                        currentReminder = null
                        openCreateReminderDialog.value = true
                    }
                }
            }
        }
    }

    CreateReminderDialog(
        openDialogState = openCreateReminderDialog,
        currentReminder = currentReminder,
        noteViewModel = noteViewModel
    )
}

@Composable
private fun Header() {

}

@Composable
private fun BottomBar(onCreateReminderClick: () -> Unit) {
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
        onClick = onCreateReminderClick
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
private fun CreateReminderDialog(
    openDialogState: MutableState<Boolean>,
    currentReminder: Reminder?,
    noteViewModel: NoteViewModel
) {
    if (openDialogState.value) {
        com.jobik.shkiper.ui.components.modals.CreateReminderDialog(
            reminderDialogProperties = if (currentReminder != null) ReminderDialogProperties(
                currentReminder.date,
                currentReminder.time,
                currentReminder.repeat
            )
            else ReminderDialogProperties(),
            onGoBack = { openDialogState.value = false },
            onDelete = {
                if (currentReminder != null) {
                    noteViewModel.deleteReminder(currentReminder._id)
                    openDialogState.value = false
                }
            },
            onSave = { date, time, repeat ->
                noteViewModel.createOrUpdateReminder(
                    reminder = currentReminder,
                    date = date,
                    time = time,
                    repeatMode = repeat
                )
                openDialogState.value = false
            },
        )
    }
}