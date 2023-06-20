package com.example.notepadapp.ui.components.modals

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.ui.components.buttons.DropDownButton
import com.example.notepadapp.ui.components.buttons.DropDownButtonSizeMode
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.components.fields.CustomDatePicker
import com.example.notepadapp.ui.components.fields.CustomTimePicker
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

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
    val pagerState = rememberPagerState()
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier.clip(RoundedCornerShape(15.dp)).background(CustomAppTheme.colors.secondaryBackground)
                .padding(20.dp)
        ) {
            HorizontalPager(
                pageCount = ReminderDialogPages.values().size,
                state = pagerState,
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
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onDelete != null)
            RoundedButton(
                text = "Delete", onClick = {
                    coroutineScope.launch {
                        onDelete()
                    }
                }, border = BorderStroke(0.dp, Color.Transparent), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent
                )
            )
        Row(
            modifier = if (onDelete == null) Modifier.fillMaxWidth() else Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedButton(
                text = if (pagerState.currentPage > 0) "Back" else "Cancel", onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        else {
                            onGoBack()
                        }
                    }
                }, border = BorderStroke(0.dp, Color.Transparent), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent
                )
            )
            if (onDelete == null) {
                Row(
                    Modifier.align(Alignment.CenterVertically), horizontalArrangement = Arrangement.Center
                ) {
                    repeat(ReminderDialogPages.values().size) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary
                        Box(
                            modifier = Modifier.padding(2.dp).clip(CircleShape).background(color).size(7.dp)
                        )
                    }
                }
            } else {
                Spacer(Modifier.width(10.dp))
            }
            RoundedButton(
                text = if (pagerState.currentPage == ReminderDialogPages.values().size - 1) "Save" else "Next",
                onClick = {
                    if (pagerState.currentPage == ReminderDialogPages.values().size - 1) {
                        onSave()
                    } else coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
            )
        }
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
    val repeatModeList = RepeatMode.values().map { it.getLocalizedValue(LocalContext.current) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            "Reminder",
            style = MaterialTheme.typography.h5,
            color = CustomAppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 15.dp).fillMaxWidth()
        )
        Column(horizontalAlignment = Alignment.Start) {
            Row(Modifier.fillMaxWidth().height(38.dp)) {
                Icon(
                    tint = CustomAppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Event,
                    contentDescription = "Event"
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    DateHelper.getLocalizedDate(date.value),
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.text,
                )
            }
            Row(Modifier.fillMaxWidth().height(38.dp)) {
                Icon(
                    tint = CustomAppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Schedule"
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    time.value.toString(),
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.text,
                )
            }
            Row(Modifier.fillMaxWidth().height(38.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    tint = CustomAppTheme.colors.textSecondary,
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat"
                )
                Spacer(Modifier.width(8.dp))
                DropDownButton(repeatModeList,
                    repeatMode.value.ordinal,
                    Modifier.width(150.dp),
                    DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH,
                    onChangedSelection = { repeatMode.value = RepeatMode.values()[it] }) {
                    RoundedButton(
                        text = repeatMode.value.getLocalizedValue(LocalContext.current),
                        onClick = { it() },
                        border = BorderStroke(1.dp, CustomAppTheme.colors.stroke),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent
                        )
                    )
                }
            }
            if (!DateHelper.isFutureDateTime(date.value, time.value))
                Text(
                    "The date must be in the future",
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.text,
                    modifier = Modifier.padding(top = 10.dp)
                )
        }
    }
}

@Composable
private fun TimePickPage(
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                DateHelper.getLocalizedDate(date.value),
                style = MaterialTheme.typography.h5,
                color = CustomAppTheme.colors.textSecondary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                time.value.toString(),
                style = MaterialTheme.typography.h5,
                color = CustomAppTheme.colors.text,
            )
        }
        Spacer(Modifier.height(20.dp))
        CustomTimePicker(time.value) { newTime -> time.value = newTime }
    }
}

@Composable
private fun DatePickPage(date: MutableState<LocalDate>) {
    Column {
        Text(
            DateHelper.getLocalizedDate(date.value),
            style = MaterialTheme.typography.h5,
            color = CustomAppTheme.colors.text
        )
        Spacer(Modifier.height(10.dp))
        CustomDatePicker(
            date.value, ContentHeightMode.Wrap
        ) { day -> date.value = day.date }
    }
}