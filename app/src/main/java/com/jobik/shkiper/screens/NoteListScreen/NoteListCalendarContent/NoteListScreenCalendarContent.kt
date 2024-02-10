package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jobik.shkiper.ui.components.fields.DaysOfWeekTitle
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.helpers.WindowWidthSizeClass
import com.jobik.shkiper.ui.helpers.isWidth
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate

@Composable
fun NoteListScreenCalendarContent(
    navController: NavController,
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
) {
    val adjacentMonths = 500L
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth }
    val endMonth = remember(currentDate) { currentMonth.plusMonths(adjacentMonths) }
    val daysOfWeek = remember { daysOfWeek() }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val weekState = rememberWeekCalendarState(
            startDate = startMonth.atStartOfMonth(),
            endDate = endMonth.atEndOfMonth(),
            firstVisibleWeekDate = currentDate,
            firstDayOfWeek = daysOfWeek.first(),
        )

        val isCompactWidthScreen = isWidth(sizeClass = WindowWidthSizeClass.Compact)
        WeekCalendar(
            calendarScrollPaged = isCompactWidthScreen,
            state = weekState,
            weekHeader = { DaysOfWeekTitle(daysOfWeek = daysOfWeek) },
            dayContent = { day ->
                CalendarDayView(
                    modifier = if (isCompactWidthScreen) Modifier.padding(4.dp) else Modifier
                        .height(60.dp)
                        .padding(4.dp),
                    day = day,
                    isSelected = day.date.isEqual(currentDate.plusDays(3)),
                    showIndicator = day.date.dayOfMonth % 4 == 0,
                ) {
                }
            },
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}