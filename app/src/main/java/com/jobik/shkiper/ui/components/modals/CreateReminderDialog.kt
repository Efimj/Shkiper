package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.helpers.areChanelNotificationsEnabled
import com.jobik.shkiper.helpers.areNotificationsEnabled
import com.jobik.shkiper.services.notification_service.NotificationScheduler
import com.jobik.shkiper.ui.components.buttons.*
import com.jobik.shkiper.ui.components.fields.CustomDatePicker
import com.jobik.shkiper.ui.components.fields.CustomTimePicker
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


private enum class ReminderDialogPages(val value: Int) {
    DATEPICK(0), TIMEPICK(1), REPEATMODE(2),
}

data class ReminderDialogProperties(
    var date: LocalDate = LocalDate.now(),
    var time: LocalTime = LocalTime.now(),
    var repeatMode: RepeatMode = RepeatMode.NONE
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateReminderDialog(
    reminderDialogProperties: ReminderDialogProperties = ReminderDialogProperties(),
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: (date: LocalDate, time: LocalTime, repeatMode: RepeatMode) -> Unit,
) {
    val date = rememberSaveable { mutableStateOf(reminderDialogProperties.date) }
    val time = rememberSaveable { mutableStateOf(reminderDialogProperties.time) }
    val repeatMode = rememberSaveable { mutableStateOf(reminderDialogProperties.repeatMode) }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        ReminderDialogPages.values().size
    }
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier.clip(RoundedCornerShape(15.dp)).background(CustomTheme.colors.secondaryBackground)
                .padding(vertical = 20.dp)
        ) {
            HorizontalPager(
                modifier = Modifier,
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
            ) {
                DialogContent(it, date, time, repeatMode)
            }
            DialogFooter(pagerState, onGoBack, onDelete) {
                onSave(date.value, time.value, repeatMode.value)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DialogFooter(
    pagerState: PagerState,
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onDelete != null)
            CustomButton(
                text = stringResource(R.string.Delete), onClick = {
                    coroutineScope.launch {
                        onDelete()
                    }
                },
                style = ButtonStyle.Text
            )
        CustomButton(
            text = if (pagerState.currentPage > 0) stringResource(R.string.Back) else stringResource(R.string.Cancel),
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    else {
                        onGoBack()
                    }
                }
            },
            style = ButtonStyle.Text
        )
        val isEnd = pagerState.currentPage == ReminderDialogPages.values().size - 1

        CustomButton(
            text = if (isEnd) stringResource(R.string.Save) else stringResource(
                R.string.Next
            ),
            onClick = {
//                if (isEnd) {
//                    onSave()
//                } else coroutineScope.launch {
//                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                }
                IntentHelper().startIntentAppNotificationSettings(
                    context = context,
                    channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                )
            },
            style = if (isEnd) ButtonStyle.Filled else ButtonStyle.Text,
            properties = if (isEnd) DefaultButtonProperties(
                buttonColors = ButtonDefaults.buttonColors(
                    backgroundColor = CustomTheme.colors.active,
                    disabledBackgroundColor = Color.Transparent
                ),
                horizontalPaddings = 10.dp,
                textColor = CustomTheme.colors.textOnActive,
                iconTint = CustomTheme.colors.textOnActive,
            ) else null
        )
    }
}

@Composable
private fun DialogContent(
    it: Int,
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
    repeatMode: MutableState<RepeatMode>,
) {
    Column(Modifier.height(340.dp)) {
        when (it) {
            ReminderDialogPages.DATEPICK.value -> DatePickPage(date)

            ReminderDialogPages.TIMEPICK.value -> TimePickPage(date, time)

            ReminderDialogPages.REPEATMODE.value -> RepeatModePage(date, time, repeatMode)
        }
    }
}

@Composable
private fun RepeatModePage(
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
    repeatMode: MutableState<RepeatMode>,
) {
    val repeatModeList = RepeatMode.values().map { DropDownItem(text = it.getLocalizedValue(LocalContext.current)) }
    val isExpanded = remember { mutableStateOf(false) }
    val isDatePast = !DateHelper.isFutureDateTime(date.value, time.value)
    val context = LocalContext.current
    val isNotificationEnabled = remember {
        mutableStateOf(
            areNotificationsEnabled(context = context) &&
                    areChanelNotificationsEnabled(
                        context = context,
                        channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                    )
        )
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            stringResource(R.string.Reminder),
            style = MaterialTheme.typography.h5,
            color = CustomTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 15.dp).fillMaxWidth()
        )
        Column(horizontalAlignment = Alignment.Start) {
            Row(Modifier.fillMaxWidth().height(38.dp)) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Event,
                    contentDescription = stringResource(R.string.Event)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    DateHelper.getLocalizedDate(date.value),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.text,
                )
            }
            Row(Modifier.fillMaxWidth().height(38.dp)) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Schedule,
                    contentDescription = stringResource(R.string.Schedule)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = time.value.toString(),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.text,
                )
            }
            if (isDatePast)
                Text(
                    text = stringResource(R.string.ErrorDateMastBeFuture),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.active,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            Row(Modifier.fillMaxWidth().height(38.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Repeat,
                    contentDescription = stringResource(R.string.Repeat)
                )
                Spacer(Modifier.width(8.dp))
                DropDownButton(
                    items = repeatModeList,
                    expanded = isExpanded,
                    selectedIndex = repeatMode.value.ordinal,
                    onChangedSelection = { repeatMode.value = RepeatMode.values()[it] }) {
                    CustomButton(
                        text = repeatMode.value.getLocalizedValue(LocalContext.current),
                        onClick = { it() },
                        style = ButtonStyle.Text
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (!isNotificationEnabled.value)
                    Text(
                        text = "NotificationDisabled",
                        style = MaterialTheme.typography.body1,
                        color = CustomTheme.colors.active,
                    )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimePickPage(
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.basicMarquee().fillMaxWidth().padding(horizontal = 20.dp)) {
            Text(
                DateHelper.getLocalizedDate(date.value),
                style = MaterialTheme.typography.h5,
                color = CustomTheme.colors.textSecondary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                time.value.toString(),
                style = MaterialTheme.typography.h5,
                color = CustomTheme.colors.text,
            )
        }
        Spacer(Modifier.height(20.dp))
        CustomTimePicker(time.value) { newTime -> time.value = newTime }
    }
}

@Composable
private fun DatePickPage(date: MutableState<LocalDate>) {
    Column(Modifier.padding(horizontal = 20.dp)) {
        Text(
            DateHelper.getLocalizedDate(date.value),
            style = MaterialTheme.typography.h5,
            color = CustomTheme.colors.text
        )
        Spacer(Modifier.height(10.dp))
        CustomDatePicker(
            date.value, ContentHeightMode.Wrap
        ) { day -> date.value = day.date }
    }
}