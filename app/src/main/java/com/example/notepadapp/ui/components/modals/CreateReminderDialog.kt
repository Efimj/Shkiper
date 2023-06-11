package com.example.notepadapp.ui.components.modals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.components.fields.CustomDatePicker
import com.example.notepadapp.ui.components.fields.CustomTimePicker
import com.example.notepadapp.ui.theme.CustomAppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

private enum class ReminderDialogPages(val value: Int) {
    DatePick(0),
    TimePick(1),
    RepeatMode(2),
    Confirmation(3),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateReminderDialog() {
    val date = remember { mutableStateOf(LocalDate.now()) }
    val time = remember { mutableStateOf(LocalTime.now()) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Dialog({}, DialogProperties(true, true)) {
        Column(Modifier.clip(RoundedCornerShape(15.dp)).background(CustomAppTheme.colors.secondaryBackground)) {
            HorizontalPager(
                pageCount = ReminderDialogPages.values().size,
                state = pagerState,
            ) {
                when (it) {
                    ReminderDialogPages.DatePick.value ->
                        CustomDatePicker(date.value) { date.value = it.date }

                    ReminderDialogPages.TimePick.value ->
                        CustomTimePicker(time.value) { time.value = it }
                }
            }
            RoundedButton(
                text = "Next",
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
            )
        }
    }
}