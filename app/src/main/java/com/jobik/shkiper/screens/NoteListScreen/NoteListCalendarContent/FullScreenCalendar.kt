package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.helpers.displayText
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.MainMenuButtonState
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun FullScreenCalendar(viewModel: CalendarViewModel) {
    val currentMonth = remember { LocalDate.now().yearMonth }
    val startMonth = remember { currentMonth }
    val endMonth = remember { currentMonth.plusMonths(200) }
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfRow
    )

    BackHandler(viewModel.screenState.value.fullScreenCalendarOpen) {
        viewModel.switchFullScreenCalendarOpen()
    }

    LaunchedEffect(viewModel.screenState.value.fullScreenCalendarOpen) {
        MainMenuButtonState.isButtonOpened.value = viewModel.screenState.value.fullScreenCalendarOpen
    }

    Column(modifier = Modifier.background(CustomTheme.colors.secondaryBackground)) {
        Header(daysOfWeek)
        VerticalCalendar(
            modifier = Modifier.fillMaxSize(),
            state = state,
            calendarScrollPaged = false,
            contentHeightMode = ContentHeightMode.Wrap,
            contentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp),
            dayContent = { day -> DayContent(day, viewModel) },
            monthHeader = { month -> MonthHeader(month) },
            monthBody = { _, content -> MonthBody(content) },
        )
    }
}

@Composable
private fun DayContent(
    day: CalendarDay,
    viewModel: CalendarViewModel
) {
    val inRange = remember(viewModel.screenState.value.selectedDateRange) {
        mutableStateOf(day.date in viewModel.screenState.value.selectedDateRange.first..viewModel.screenState.value.selectedDateRange.second)
    }

    CalendarDayView(
        day = day,
        isSelected = viewModel.screenState.value.selectedDateRange.first == day.date || viewModel.screenState.value.selectedDateRange.second == day.date,
        showIndicator = day.date in viewModel.screenState.value.datesWithIndicator,
        inRange = inRange.value
    ) {
        if (viewModel.screenState.value.selectedDateRange.first == viewModel.screenState.value.selectedDateRange.second)
            viewModel.selectNextDate(date = day.date)
        else
            viewModel.selectDate(date = day.date)
    }
}

@Composable
private fun MonthBody(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        content()
    }
}

@Composable
private fun MonthHeader(month: CalendarMonth) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = month.yearMonth.displayText(),
            color = CustomTheme.colors.text,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Header(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = CustomTheme.colors.textSecondary,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}