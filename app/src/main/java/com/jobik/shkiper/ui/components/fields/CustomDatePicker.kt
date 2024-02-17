package com.jobik.shkiper.ui.components.fields

import android.view.RoundedCorner
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
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
    currentDate: LocalDate,
    contentHeightMode: ContentHeightMode = ContentHeightMode.Wrap,
    onDateChange: (CalendarDay) -> Unit,
) {
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
        dayContent = { CalendarDayView(it, currentDate, onDateChange) },
        monthHeader = { month ->
            MonthHeader(month)
        },
        contentHeightMode = contentHeightMode
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
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = formattedDate,
            color = CustomTheme.colors.text,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = CustomTheme.colors.text,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}