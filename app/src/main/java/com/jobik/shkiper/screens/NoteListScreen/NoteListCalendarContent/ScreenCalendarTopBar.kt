package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.fields.DaysOfWeekTitle
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.components.layouts.CalendarDayViewRangeStyle
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.helpers.*
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun ScreenCalendarTopBar(
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
) {
    val adjacentMonths = 500L
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth }
    val endMonth = remember(currentDate) { currentMonth.plusMonths(adjacentMonths) }
    val daysOfWeek = remember { daysOfWeek() }

    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first(),
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(CustomTheme.colors.secondaryBackground)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CustomTopAppBar(
            text = rememberFirstVisibleWeekAfterScroll(weekState).days.first().date.yearMonth.displayText(),
            navigation = TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.ArrowBack,
                iconDescription = R.string.GoBack,
                onClick = onSlideBack
            ),
            items = listOf(
                TopAppBarItem(
                    isActive = false,
                    icon = Icons.Outlined.CalendarMonth,
                    iconDescription = R.string.AttachNote,
                    onClick = viewModel::switchFullScreenCalendarOpen
                ),
            )
        )

        val isCompactWidthScreen = isWidth(sizeClass = WindowWidthSizeClass.Compact)

        WeekCalendar(
            modifier = Modifier.horizontalWindowInsetsPadding(),
            calendarScrollPaged = isCompactWidthScreen,
            state = weekState,
            weekHeader = { DaysOfWeekTitle(daysOfWeek = daysOfWeek) },
            contentPadding = PaddingValues(horizontal = 10.dp),
            dayContent = { day ->
                val inRange = remember(viewModel.screenState.value.selectedDateRange) {
                    mutableStateOf(
                        inRange(date = day.date, range = viewModel.screenState.value.selectedDateRange)
                    )
                }

                CalendarDayView(
                    modifier =
                    if (isCompactWidthScreen)
                        Modifier.padding(4.dp)
                    else Modifier
                        .height(60.dp)
                        .padding(4.dp),
                    day = day,
                    rangeStyle = inRange.value,
                    isSelected = viewModel.screenState.value.selectedDateRange.first == day.date || viewModel.screenState.value.selectedDateRange.second == day.date,
                    showIndicator = day.date in viewModel.screenState.value.datesWithIndicator,
                ) {
                    viewModel.selectDate(date = it.date)
                }
            },
        )
    }
}

private fun inRange(date: LocalDate, range: Pair<LocalDate, LocalDate>): CalendarDayViewRangeStyle? {
    if (date !in range.first..range.second) return null
    if (range.first == range.second) return null
    return CalendarDayViewRangeStyle.Rounded
}