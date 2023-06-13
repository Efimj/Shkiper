package com.example.notepadapp.ui.components.modals.CreateReminderDialog

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.page.NoteListPage.NotesViewModel
import com.example.notepadapp.ui.components.buttons.DropDownButton
import com.example.notepadapp.ui.components.buttons.DropDownButtonSizeMode
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.components.fields.CustomDatePicker
import com.example.notepadapp.ui.components.fields.CustomTimePicker
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

private enum class ReminderDialogPages(val value: Int) {
    DATEPICK(0),
    TIMEPICK(1),
    REPEATMODE(2),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateReminderDialog(noteIds: List<ObjectId>, reminderDialogViewModel: ReminderDialogViewModel = hiltViewModel()) {
    reminderDialogViewModel.setNoteIds(noteIds)
    val date = remember { mutableStateOf(LocalDate.now()) }
    val time = remember { mutableStateOf(LocalTime.now()) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Dialog({}, DialogProperties(true, true)) {
        Column(
            Modifier.clip(RoundedCornerShape(15.dp)).background(CustomAppTheme.colors.secondaryBackground)
                .padding(20.dp)
        ) {
            HorizontalPager(
                pageCount = ReminderDialogPages.values().size,
                state = pagerState,
            ) {
                DialogContent(it, date, time)
            }
            DialogFooter(coroutineScope, pagerState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DialogFooter(
    coroutineScope: CoroutineScope,
    pagerState: PagerState
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(
            text = if (pagerState.currentPage > 0) "Back" else "Cancel",
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage > 0)
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    else {
                    }
                }
            },
            border = BorderStroke(0.dp, Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        )
        Row(
            Modifier.align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(ReminderDialogPages.values().size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration)
                        CustomAppTheme.colors.text else CustomAppTheme.colors.textSecondary
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(7.dp)
                )
            }
        }
        RoundedButton(
            text = if (pagerState.currentPage == ReminderDialogPages.values().size - 1) "Save" else "Next",
            onClick = {
                if (pagerState.currentPage == ReminderDialogPages.values().size - 1) {

                } else
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
            },
        )
    }
}

@Composable
private fun DialogContent(
    it: Int,
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>
) {
    Column(Modifier.height(340.dp)) {
        when (it) {
            ReminderDialogPages.DATEPICK.value ->
                DatePickPage(date)

            ReminderDialogPages.TIMEPICK.value ->
                TimePickPage(time, date)

            ReminderDialogPages.REPEATMODE.value ->
                RepeatModePage(time, date)
        }
    }
}

@Composable
private fun RepeatModePage(time: MutableState<LocalTime>, date: MutableState<LocalDate>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val repeatModes = RepeatMode.values().map { it.getLocalizedValue(LocalContext.current) }
        var selectedIndex by remember { mutableStateOf(0) }
        DropDownButton(
            repeatModes,
            selectedIndex,
            Modifier.weight(1f),
            DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH
        ) {
            selectedIndex = it
        }
    }
}

@Composable
private fun TimePickPage(time: MutableState<LocalTime>, date: MutableState<LocalDate>) {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())
    val formattedDate = date.value.format(formatter)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth()) {
            Text(formattedDate, style = MaterialTheme.typography.h5, color = CustomAppTheme.colors.textSecondary)
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
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())
    val formattedDate = date.value.format(formatter)
    Column {
        Text(formattedDate, style = MaterialTheme.typography.h5, color = CustomAppTheme.colors.text)
        Spacer(Modifier.height(10.dp))
        CustomDatePicker(date.value, ContentHeightMode.Wrap) { day -> date.value = day.date }
    }
}