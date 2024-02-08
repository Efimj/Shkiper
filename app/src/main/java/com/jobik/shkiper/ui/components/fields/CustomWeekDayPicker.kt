package com.jobik.shkiper.ui.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent.CalendarHeader
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate

@Composable
fun CustomWeekDayPicker() {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth }
    val endMonth = remember(currentDate) { currentMonth.plusYears(3) }
    val daysOfWeek = remember { daysOfWeek() }

    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first(),
    )

    WeekCalendar(
        state = weekState,
        weekHeader = { CalendarHeader(daysOfWeek = daysOfWeek) },
        dayContent = { day ->
//            val isSelectable = day.position == WeekDayPosition.RangeDate
//            Day(
//                day.date,
//                isSelected = isSelectable && selections.contains(day.date),
//                isSelectable = isSelectable,
//            ) { clicked ->
//                if (selections.contains(clicked)) {
//                    selections.remove(clicked)
//                } else {
//                    selections.add(clicked)
//                }
//            }
        },
    )
}

