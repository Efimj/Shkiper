package com.example.notepadapp.ui.components.fields

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CustomDatePicker(

) {
    val currentDate = remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth } // Adjust as needed currentMonth.minusMonths(100)
    val endMonth = remember { currentMonth.plusYears(3) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = { CalendarDayView(it, currentDate.value) { currentDate.value = it.date } },
        monthHeader = { month ->
            // You may want to use `remember {}` here so the mapping is not done
            // every time as the days of week order will never change unless
            // you set a new value for `firstDayOfWeek` in the state.
            MonthHeader(month)
        }
    )
}

@Composable
private fun MonthHeader(month: CalendarMonth) {
    val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
    Column {
        MonthTitle(month)
        Spacer(Modifier.padding(bottom = 10.dp))
        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
    }
}

@Composable
fun MonthTitle(month: CalendarMonth) {
    val current = month.yearMonth.atDay(1)
    val formatter = DateTimeFormatter.ofPattern("MM/yyyy").withLocale(Locale.getDefault())
    val formattedDate = current.format(formatter)
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = 15.dp, end = 5.dp),
            textAlign = TextAlign.Center,
            text = formattedDate,
            color = CustomAppTheme.colors.text,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun CalendarDayView(day: CalendarDay, currentDate: LocalDate, onClick: (CalendarDay) -> Unit) {
    val dateNow = LocalDate.now()
    val isDateCurrentOrFuture = isDateCurrentOrFuture(day.date, dateNow)
    val borderCornerShape = RoundedCornerShape(15.dp)

    Box(
        modifier = Modifier
            .clip(borderCornerShape)
            .border(
                BorderStroke(
                    1.dp,
                    if (currentDate == day.date) CustomAppTheme.colors.active else
                        if (day.date == dateNow) CustomAppTheme.colors.stroke else
                            Color.Transparent
                ),
                borderCornerShape
            )
            .background(if (currentDate == day.date) CustomAppTheme.colors.secondaryBackground else Color.Transparent)
            .aspectRatio(1f)
            .clickable(
                enabled = day.position == DayPosition.MonthDate && isDateCurrentOrFuture,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.body1,
            color = if (day.position == DayPosition.MonthDate)
                if (isDateCurrentOrFuture) CustomAppTheme.colors.text
                else CustomAppTheme.colors.textSecondary
            else Color.Transparent
        )
    }
}

fun isDateCurrentOrFuture(date: LocalDate, currentDate: LocalDate = LocalDate.now()): Boolean {
    return !date.isBefore(currentDate)
}