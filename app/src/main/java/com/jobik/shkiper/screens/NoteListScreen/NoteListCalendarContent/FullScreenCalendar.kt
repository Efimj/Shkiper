package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.components.layouts.CalendarDayViewRangeStyle
import com.jobik.shkiper.ui.helpers.displayText
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.AppNavigationBarState
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
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
        AppNavigationBarState.isVisible.value = viewModel.screenState.value.fullScreenCalendarOpen
    }

    Box {
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    modifier = Modifier
                        .heightIn(min = 50.dp)
                        .widthIn(max = 350.dp)
                        .fillMaxWidth(),
                    shape = CustomTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CustomTheme.colors.textOnActive,
                        containerColor = CustomTheme.colors.active
                    ),
                    border = null,
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = viewModel::switchFullScreenCalendarOpen
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = stringResource(R.string.Confirm),
                        tint = CustomTheme.colors.textOnActive
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.Apply),
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
}

@Composable
private fun DayContent(
    day: CalendarDay,
    viewModel: CalendarViewModel
) {
    val inRange = remember(viewModel.screenState.value.selectedDateRange) {
        mutableStateOf(
            inRange(date = day.date, range = viewModel.screenState.value.selectedDateRange)
        )
    }

    CalendarDayView(
        modifier = Modifier.padding(vertical = 4.dp),
        day = day,
        isSelected = viewModel.screenState.value.selectedDateRange.first == day.date || viewModel.screenState.value.selectedDateRange.second == day.date,
        showIndicator = day.date in viewModel.screenState.value.datesWithIndicator,
        rangeStyle = inRange.value
    ) {
        if (viewModel.screenState.value.selectedDateRange.first == viewModel.screenState.value.selectedDateRange.second)
            viewModel.selectNextDate(date = day.date)
        else
            viewModel.selectDate(date = day.date)
    }
}

private fun inRange(date: LocalDate, range: Pair<LocalDate, LocalDate>): CalendarDayViewRangeStyle? {
    if (date !in range.first..range.second) return null
    if (range.first == range.second) return null

    val weekFields = WeekFields.of(Locale.getDefault())

    val isDateStart = date == range.first
    val isDateEnd = date == range.second
    val isWeekStart = date.dayOfWeek == weekFields.firstDayOfWeek
    val isWeekEnd = date.dayOfWeek == weekFields.firstDayOfWeek.plus(6)

    val isMonthStart = date.withDayOfMonth(1) == date
    val isMonthEnd = date.withDayOfMonth(date.month.length(date.isLeapYear)) == date

    if (isDateEnd && isMonthStart) return null
    if (isDateStart && isMonthEnd) return null
    if (isWeekStart && isDateEnd) return null
    if (isWeekEnd && isDateStart) return null

    if (isWeekStart && isMonthEnd) return CalendarDayViewRangeStyle.Rounded
    if (isWeekEnd && isMonthStart) return CalendarDayViewRangeStyle.Rounded

    if (isWeekStart || isMonthStart) return CalendarDayViewRangeStyle.LeftRound
    if (isWeekEnd || isMonthEnd) return CalendarDayViewRangeStyle.RightRound
    if (isDateStart) return CalendarDayViewRangeStyle.LeftRound
    if (isDateEnd) return CalendarDayViewRangeStyle.RightRound
    return CalendarDayViewRangeStyle.Square
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