package com.jobik.shkiper.screens.note

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.ui.components.cards.ReminderCard
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenRemindersContent(noteViewModel: NoteViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val reminderSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentReminder = rememberSaveable { mutableStateOf<Reminder?>(null) }
    val openCreateReminderDialog = rememberSaveable { mutableStateOf(false) }
    val selectedReminderIds = rememberSaveable { mutableStateOf<List<ObjectId>>(emptyList()) }

    LaunchedEffect(noteViewModel.screenState.value.isReminderMenuOpen) {
        if (!noteViewModel.screenState.value.isReminderMenuOpen) {
            coroutineScope.launch { reminderSheetState.hide() }.invokeOnCompletion {
                currentReminder.value = null
                openCreateReminderDialog.value = false
                selectedReminderIds.value = emptyList()
            }
        } else {
            if (noteViewModel.screenState.value.reminders.isEmpty()) {
                openCreateReminderDialog.value = true
            }
        }
    }

    val verticalInsets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
    val headerInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
    val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
    val bottomInsetsDp = bottomInsets.asPaddingValues().calculateBottomPadding()
    val listVerticalPadding = 10.dp
    val listVerticalWithToolBarPadding = 80.dp

    val topListPaddingValues =
        if (selectedReminderIds.value.isEmpty()) listVerticalPadding else listVerticalWithToolBarPadding
    val topPadding by animateDpAsState(targetValue = topListPaddingValues, label = "topPadding")

    val bottomListPaddingValues =
        if (selectedReminderIds.value.isEmpty()) listVerticalWithToolBarPadding + bottomInsetsDp else listVerticalPadding + bottomInsetsDp
    val bottomPadding by animateDpAsState(
        targetValue = bottomListPaddingValues,
        label = "bottomPadding"
    )

    if (noteViewModel.screenState.value.isReminderMenuOpen) {
        val topInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)

        ModalBottomSheet(
            sheetState = reminderSheetState,
            dragHandle = null,
            containerColor = Color.Transparent,
            contentColor = AppTheme.colors.text,
//            contentWindowInsets = {WindowInsets.ime},
            windowInsets = WindowInsets.ime,
            onDismissRequest = {
                noteViewModel.switchReminderDialogShow(false)
            },
        ) {
            Spacer(modifier = Modifier.windowInsetsPadding(topInsets))
            Surface(
                shape = BottomSheetDefaults.ExpandedShape,
                contentColor = AppTheme.colors.text,
                color = AppTheme.colors.background,
                tonalElevation = BottomSheetDefaults.Elevation,
            ) {
                Box {
                    AnimatedContent(
                        targetState = noteViewModel.screenState.value.reminders.isEmpty(),
                        transitionSpec = {
                            if (initialState) {
                                (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> -height } + fadeOut())
                            } else {
                                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> height } + fadeOut())
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, label = ""
                    ) {
                        if (it) {
                            EmptyRemindersContent(
                                modifier = Modifier.windowInsetsPadding(
                                    verticalInsets
                                )
                            )
                        } else {
                            RemindersList(
                                topPadding = topPadding,
                                bottomPadding = bottomPadding,
                                noteViewModel = noteViewModel,
                                currentReminder = currentReminder,
                                selectedReminderIds = selectedReminderIds,
                                openCreateReminderDialog = openCreateReminderDialog
                            )
                        }
                    }
                    Header(
                        modifier = Modifier,
                        noteViewModel = noteViewModel,
                        selectedReminderIds = selectedReminderIds
                    )
                    BottomBar(
                        modifier = Modifier.windowInsetsPadding(bottomInsets),
                        isHidden = selectedReminderIds.value.isNotEmpty()
                    ) {
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

@Composable
private fun RemindersList(
    noteViewModel: NoteViewModel,
    topPadding: Dp,
    bottomPadding: Dp,
    currentReminder: MutableState<Reminder?>,
    selectedReminderIds: MutableState<List<ObjectId>>,
    openCreateReminderDialog: MutableState<Boolean>
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = topPadding,
            bottom = bottomPadding
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = noteViewModel.screenState.value.reminders,
            key = { it._id.toHexString() }) { reminder ->
            ReminderCard(
                reminder = reminder,
                isSelected = selectedReminderIds.value.contains(reminder._id),
                onClick = {
                    onReminderClick(
                        selectedReminderIds,
                        reminder,
                        currentReminder,
                        openCreateReminderDialog
                    )
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
private fun EmptyRemindersContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp, top = 30.dp)
            .heightIn(max = 240.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.NotificationsOff,
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(90.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.NoReminders),
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.text,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BoxScope.Header(
    modifier: Modifier,
    noteViewModel: NoteViewModel,
    selectedReminderIds: MutableState<List<ObjectId>>
) {
    val clearSelectedReminders = { selectedReminderIds.value = emptyList() }

    Box(modifier = modifier.align(Alignment.TopCenter)) {
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
                    .padding(top = 10.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = AppTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppTheme.colors.text,
                        containerColor = AppTheme.colors.container
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    modifier = Modifier.fillMaxHeight(),
                    shape = AppTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppTheme.colors.text,
                        containerColor = AppTheme.colors.container
                    ),
                    border = null,
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = clearSelectedReminders
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.Back),
                        tint = AppTheme.colors.text
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BottomBar(
    modifier: Modifier,
    isHidden: Boolean,
    onCreateReminderClick: () -> Unit
) {
    Box(
        modifier = modifier
            .align(Alignment.BottomCenter)
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
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.onPrimary,
                    containerColor = AppTheme.colors.primary
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = onCreateReminderClick
            ) {
                Text(
                    text = stringResource(R.string.CreateReminder),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onPrimary,
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
                        it.repeat,
                        it.icon,
                        it.color
                    )
                } else {
                    ReminderDialogProperties()
                }
            },
            onGoBack = { openDialogState.value = false },
            onDelete = if (currentReminder.value != null) {
                {
                    currentReminder.value.let {
                        if (it != null) {
                            noteViewModel.deleteReminder(it._id)
                            clearCurrentReminder()
                        }
                    }
                }
            } else null,
            onSave = { props ->
                noteViewModel.createOrUpdateReminder(
                    reminder = currentReminder.value,
                    props = props
                )
                clearCurrentReminder()
            },
        )
    }
}