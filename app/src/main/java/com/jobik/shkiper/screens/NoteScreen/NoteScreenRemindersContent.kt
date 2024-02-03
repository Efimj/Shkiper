package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
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
import com.jobik.shkiper.ui.components.cards.ReminderCard
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.CustomTheme
import org.mongodb.kbson.ObjectId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val shareSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentReminder = rememberSaveable { mutableStateOf<Reminder?>(null) }
    val openCreateReminderDialog = rememberSaveable { mutableStateOf(false) }
    val selectedReminderIds = rememberSaveable { mutableStateOf<List<ObjectId>>(emptyList()) }

    LaunchedEffect(noteViewModel.screenState.value.isReminderMenuOpen) {
        if (!noteViewModel.screenState.value.isReminderMenuOpen) {
            currentReminder.value = null
            openCreateReminderDialog.value = false
            selectedReminderIds.value = emptyList()
            shareSheetState.hide()
        } else {
            if (noteViewModel.screenState.value.reminders.isEmpty()) {
                openCreateReminderDialog.value = true
            }
        }
    }

    if (noteViewModel.screenState.value.isReminderMenuOpen) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchReminderDialogShow()
            },
            dragHandle = null,
        ) {
            Box {
                Column {
                    Header(
                        noteViewModel = noteViewModel,
                        selectedReminderIds = selectedReminderIds
                    )
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
                            RemindersList(
                                noteViewModel = noteViewModel,
                                currentReminder = currentReminder,
                                selectedReminderIds = selectedReminderIds,
                                openCreateReminderDialog = openCreateReminderDialog
                            )
                        }
                    }
                }
                BottomBar(isHidden = selectedReminderIds.value.isNotEmpty()) {
                    currentReminder.value = null
                    openCreateReminderDialog.value = true
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
private fun RemindersList(
    noteViewModel: NoteViewModel,
    currentReminder: MutableState<Reminder?>,
    selectedReminderIds: MutableState<List<ObjectId>>,
    openCreateReminderDialog: MutableState<Boolean>
) {
    val lazyListState = rememberLazyListState()

    val topPaddingValues = if (selectedReminderIds.value.isEmpty()) 20.dp else 10.dp
    val topPadding by animateDpAsState(targetValue = topPaddingValues, label = "topPadding")

    val bottomPaddingValues = if (selectedReminderIds.value.isEmpty()) 80.dp else 10.dp
    val bottomPadding by animateDpAsState(targetValue = bottomPaddingValues, label = "bottomPadding")

    LazyColumn(
        modifier = Modifier,
        state = lazyListState,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = topPadding, bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = noteViewModel.screenState.value.reminders,
            key = { it._id.toHexString() }) { reminder ->
            ReminderCard(
                reminder = reminder,
                isSelected = selectedReminderIds.value.contains(reminder._id),
                onClick = {
                    onReminderClick(selectedReminderIds, reminder, currentReminder, openCreateReminderDialog)
                },
                onLongClick = {
                    onReminderLongClick(selectedReminderIds, reminder)
                })
        }
    }
}

private fun onReminderLongClick(
    selectedReminderIds: MutableState<List<ObjectId>>,
    reminder: Reminder
) {
    selectedReminderIds.value = selectedReminderIds.value.let {
        if (it.contains(reminder._id)) it - reminder._id else it + reminder._id
    }
}

private fun onReminderClick(
    selectedReminderIds: MutableState<List<ObjectId>>,
    reminder: Reminder,
    currentReminder: MutableState<Reminder?>,
    openCreateReminderDialog: MutableState<Boolean>
) {
    if (selectedReminderIds.value.isNotEmpty()) {
        onReminderLongClick(selectedReminderIds, reminder)
    } else {
        currentReminder.value = reminder
        openCreateReminderDialog.value = true
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
private fun Header(
    noteViewModel: NoteViewModel,
    selectedReminderIds: MutableState<List<ObjectId>>
) {
    val clearSelectedReminders = { selectedReminderIds.value = emptyList() }

    AnimatedVisibility(
        visible = selectedReminderIds.value.isNotEmpty(),
        enter = slideInVertically() + expandVertically(
            clip = false
        ) + fadeIn(),
        exit = slideOutVertically() + shrinkVertically(
            clip = false
        ) + fadeOut(),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 10.dp, top = 20.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterHorizontally)
        ) {
            Button(
                modifier = Modifier.fillMaxHeight(),
                shape = CustomTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomTheme.colors.text,
                    containerColor = CustomTheme.colors.secondaryBackground
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = clearSelectedReminders
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.Back),
                    tint = CustomTheme.colors.text
                )
            }
            Button(
                modifier = Modifier.fillMaxSize(),
                shape = CustomTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomTheme.colors.text,
                    containerColor = CustomTheme.colors.secondaryBackground
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    noteViewModel.deleteReminder(reminderIds = selectedReminderIds.value)
                    clearSelectedReminders()

                }
            ) {
                Text(
                    text = stringResource(R.string.Delete),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = CustomTheme.colors.textOnActive,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BoxScope.BottomBar(isHidden: Boolean, onCreateReminderClick: () -> Unit) {
    Box(
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        AnimatedVisibility(
            visible = isHidden.not(),
            enter = slideInVertically { it / 2 } + expandVertically(
                expandFrom = Alignment.Top,
                clip = false
            ) + fadeIn(),
            exit = slideOutVertically { -it / 2 } + shrinkVertically(
                shrinkTowards = Alignment.Top,
                clip = false
            ) + fadeOut(),
        )
        {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 10.dp)
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