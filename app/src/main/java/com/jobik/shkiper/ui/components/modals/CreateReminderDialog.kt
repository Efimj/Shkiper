package com.jobik.shkiper.ui.components.modals

import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.*
import com.jobik.shkiper.services.notification_service.NotificationScheduler
import com.jobik.shkiper.ui.components.buttons.DropDownButton
import com.jobik.shkiper.ui.components.buttons.DropDownItem
import com.jobik.shkiper.ui.components.fields.CustomDatePicker
import com.jobik.shkiper.ui.components.fields.CustomTimePicker
import com.jobik.shkiper.ui.theme.AppTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Keep
private enum class ReminderDialogPages(val value: Int) {
    DATEPICK(0), TIMEPICK(1), REPEATMODE(2),
}

@Parcelize
data class ReminderDialogProperties(
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val repeatMode: RepeatMode = RepeatMode.NONE
) : Parcelable

@Parcelize
private data class ReminderDialogState(
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val isNotificationEnabled: Boolean = false,
    val canSave: Boolean = false,
) : Parcelable

@Composable
fun CreateReminderDialog(
    reminderDialogProperties: ReminderDialogProperties = ReminderDialogProperties(),
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: (date: LocalDate, time: LocalTime, repeatMode: RepeatMode) -> Unit,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        ReminderDialogPages.entries.size
    }

    val reminderDialogState =
        rememberSaveable {
            mutableStateOf(
                ReminderDialogState(
                    date = reminderDialogProperties.date,
                    time = reminderDialogProperties.time,
                    repeatMode = reminderDialogProperties.repeatMode,
                    isNotificationEnabled = checkIsNotificationEnabled(context)
                )
            )
        }

    Dialog(
        onDismissRequest = onGoBack,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(15.dp))
                .background(AppTheme.colors.background)
                .padding(vertical = 20.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = false,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                pageSize = PageSize.Fill,
            ) {
                DialogContent(
                    it = it,
                    reminderDialogState = reminderDialogState
                )
            }
            DialogFooter(
                pagerState = pagerState,
                reminderDialogState = reminderDialogState,
                onGoBack = onGoBack,
                onDelete = onDelete,
            ) {
                onSave(
                    reminderDialogState.value.date,
                    reminderDialogState.value.time,
                    reminderDialogState.value.repeatMode
                )
            }
        }
    }
}

private fun checkIsNotificationEnabled(context: Context) =
    areNotificationsEnabled(context = context) &&
            areEXACTNotificationsEnabled(context = context) &&
            areChanelNotificationsEnabled(
                context = context,
                channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
            )

@Composable
private fun DialogFooter(
    pagerState: PagerState,
    reminderDialogState: MutableState<ReminderDialogState>,
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
) {
    val isEnd = pagerState.currentPage == ReminderDialogPages.entries.size - 1
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isFutureDate =
        remember(reminderDialogState.value.date, reminderDialogState.value.time) {
            mutableStateOf(
                DateHelper.isFutureDateTime(
                    date = reminderDialogState.value.date,
                    time = reminderDialogState.value.time
                )
            )
        }

    val buttonNextContentColor: Color by animateColorAsState(
        targetValue = if (isEnd && reminderDialogState.value.isNotificationEnabled && isFutureDate.value) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "buttonNextContentColor"
    )

    val buttonNextBackgroundColor: Color by animateColorAsState(
        targetValue = if (isEnd && reminderDialogState.value.isNotificationEnabled && isFutureDate.value) AppTheme.colors.primary else AppTheme.colors.container,
        label = "buttonNextBackgroundColor"
    )

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(
            20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = onDelete != null,
            enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
            exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
        ) {
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
                onClick = {
                    if (onDelete != null)
                        coroutineScope.launch {
                            onDelete()
                        }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsOff,
                    contentDescription = stringResource(R.string.Delete),
                    tint = AppTheme.colors.text
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.5f),
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.text,
                    containerColor = AppTheme.colors.container
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        else {
                            onGoBack()
                        }
                    }
                },
            ) {
                AnimatedContent(
                    targetState = pagerState.currentPage > 0,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Back),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Cancel),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.5f),
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = buttonNextContentColor,
                    containerColor = buttonNextBackgroundColor
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    if (isEnd && isFutureDate.value) {
                        if (!checkIsNotificationEnabled(context = context) && !reminderDialogState.value.isNotificationEnabled) {
                            enableNotificationIfDisabled(context = context)
                        } else {
                            reminderDialogState.value =
                                reminderDialogState.value.copy(isNotificationEnabled = true)
                            onSave()
                        }
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
            ) {
                AnimatedContent(
                    targetState = isEnd,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Save),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = buttonNextContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Next),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = buttonNextContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

fun enableNotificationIfDisabled(context: Context) {
    if (!areNotificationsEnabled(context = context) || !areChanelNotificationsEnabled(
            context = context,
            channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
        )
    ) {
        IntentHelper().startIntentAppNotificationSettings(
            context = context,
            channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
        )
    }
    if (!areEXACTNotificationsEnabled(context = context)) {
        IntentHelper().startIntentAppEXACTNotificationSettings(context)
    }
}

@Composable
private fun DialogContent(
    it: Int,
    reminderDialogState: MutableState<ReminderDialogState>,
) {
    Column(
        Modifier
            .animateContentSize()
            .heightIn(min = 350.dp)
    ) {
        when (it) {
            ReminderDialogPages.DATEPICK.value -> DatePickPage(reminderDialogState = reminderDialogState)
            ReminderDialogPages.TIMEPICK.value -> TimePickPage(reminderDialogState = reminderDialogState)
            ReminderDialogPages.REPEATMODE.value -> RepeatModePage(reminderDialogState = reminderDialogState)
        }
    }
}

@Composable
private fun RepeatModePage(
    reminderDialogState: MutableState<ReminderDialogState>
) {
    val repeatModeList =
        RepeatMode.entries.map { DropDownItem(text = it.getLocalizedValue(LocalContext.current)) }
    val isExpanded = remember { mutableStateOf(false) }
    val isDatePast = DateHelper.isFutureDateTime(
        date = reminderDialogState.value.date,
        time = reminderDialogState.value.time
    ).not()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            stringResource(R.string.Reminder),
            style = MaterialTheme.typography.headlineMedium,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
        Column(horizontalAlignment = Alignment.Start) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    tint = AppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Event,
                    contentDescription = stringResource(R.string.Event)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = DateHelper.getLocalizedDate(reminderDialogState.value.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.text,
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    tint = AppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Schedule,
                    contentDescription = stringResource(R.string.Schedule)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = reminderDialogState.value.time.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.text,
                )
            }
            if (isDatePast)
                Text(
                    text = stringResource(R.string.ErrorDateMastBeFuture),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = AppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Repeat,
                    contentDescription = stringResource(R.string.Repeat)
                )
                Spacer(Modifier.width(8.dp))
                DropDownButton(
                    items = repeatModeList,
                    expanded = isExpanded,
                    selectedIndex = reminderDialogState.value.repeatMode.ordinal,
                    onChangedSelection = {
                        reminderDialogState.value =
                            reminderDialogState.value.copy(repeatMode = RepeatMode.entries.toTypedArray()[it])
                    }) {
                    Button(
                        modifier = Modifier.heightIn(min = 40.dp),
                        shape = AppTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = AppTheme.colors.text,
                            containerColor = AppTheme.colors.container
                        ),
                        border = null,
                        elevation = null,
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        onClick = { it() },
                    ) {
                        Text(
                            text = reminderDialogState.value.repeatMode.getLocalizedValue(
                                LocalContext.current
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            if (!reminderDialogState.value.isNotificationEnabled)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = AppTheme.colors.primary,
                            imageVector = Icons.Default.Warning,
                            contentDescription = stringResource(R.string.TurnOnNotifications)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.TurnOnNotifications),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.colors.text,
                        )
                    }
                }
        }
    }
}

@Composable
private fun TimePickPage(
    reminderDialogState: MutableState<ReminderDialogState>
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .basicMarquee()
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = DateHelper.getLocalizedDate(reminderDialogState.value.date),
                style = MaterialTheme.typography.headlineMedium,
                color = AppTheme.colors.textSecondary,
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = reminderDialogState.value.time.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = AppTheme.colors.text,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
        Spacer(Modifier.height(20.dp))
        CustomTimePicker(startTime = reminderDialogState.value.time) { newTime ->
            reminderDialogState.value = reminderDialogState.value.copy(time = newTime)
        }
    }
}

@Composable
private fun DatePickPage(reminderDialogState: MutableState<ReminderDialogState>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = DateHelper.getLocalizedDate(reminderDialogState.value.date),
            style = MaterialTheme.typography.headlineMedium,
            color = AppTheme.colors.text,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        CustomDatePicker(
            currentDate = reminderDialogState.value.date, contentHeightMode = ContentHeightMode.Wrap
        ) { day ->
            reminderDialogState.value = reminderDialogState.value.copy(date = day.date)
        }
    }
}