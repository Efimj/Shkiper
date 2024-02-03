package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.*
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
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.components.cards.ReminderCard
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val shareSheetState = rememberModalBottomSheetState()
    val currentReminder = rememberSaveable { mutableStateOf<Reminder?>(null) }
    val openCreateReminderDialog = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(noteViewModel.screenState.value.isReminderMenuNeeded) {
        if (!noteViewModel.screenState.value.isReminderMenuNeeded) {
            currentReminder.value = null
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
                    AnimatedContent(
                        targetState = noteViewModel.screenState.value.reminders.isEmpty(),
                        transitionSpec = {
                            if (initialState) {
                                (slideInVertically { height -> height } + fadeIn()).togetherWith(slideOutVertically { height -> -height } + fadeOut())
                            } else {
                                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, label = ""
                    ) {
                        if (it) {
                            EmptyRemindersContent()
                        } else {
                            RemindersList(noteViewModel, currentReminder, openCreateReminderDialog)
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
                        currentReminder.value = null
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


fun sortReminders(reminders: List<Reminder>): List<Reminder> {
    val currentTime = LocalTime.now()
    val currentDate = LocalDate.now()

    val comparator = compareBy<Reminder>(
        { DateHelper.nextDateWithRepeating(it.date, it.time, it.repeat) },
    )

    return reminders.sortedWith(comparator)
//    return reminders
}

@Composable
private fun RemindersList(
    noteViewModel: NoteViewModel,
    currentReminder: MutableState<Reminder?>,
    openCreateReminderDialog: MutableState<Boolean>
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = sortReminders(noteViewModel.screenState.value.reminders)) { item ->
            ReminderCard(reminder = item) {
                currentReminder.value = item
                openCreateReminderDialog.value = true
            }
        }
    }
}

@Composable
private fun EmptyRemindersContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp, top = 30.dp)
            .heightIn(max = 240.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.NotificationsOff,
            contentDescription = null,
            tint = CustomTheme.colors.active,
            modifier = Modifier.size(90.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.NoReminders),
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = CustomTheme.colors.text
        )
    }
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
    currentReminder: MutableState<Reminder?>,
    noteViewModel: NoteViewModel
) {
    val clearCurrentReminder = {
        openDialogState.value = false
        currentReminder.value = null
    }

    if (openDialogState.value) {
        com.jobik.shkiper.ui.components.modals.CreateReminderDialog(
            reminderDialogProperties = currentReminder.value.let {
                if (it != null) {
                    ReminderDialogProperties(
                        it.date,
                        it.time,
                        it.repeat
                    )
                } else {
                    ReminderDialogProperties()
                }
            },
            onGoBack = { openDialogState.value = false },
            onDelete = {
                currentReminder.value.let {
                    if (it != null) {
                        noteViewModel.deleteReminder(it._id)
                        clearCurrentReminder()
                    } else {
                        ReminderDialogProperties()
                    }
                }
            },
            onSave = { date, time, repeat ->
                noteViewModel.createOrUpdateReminder(
                    reminder = currentReminder.value,
                    date = date,
                    time = time,
                    repeatMode = repeat
                )
                clearCurrentReminder()
            },
        )
    }
}