package com.jobik.shkiper.screens.NoteListScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun NoteListScreenCalendarContent(adjacentMonths: Long = 500) {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth }
    val endMonth = remember(currentDate) { currentMonth.plusMonths(adjacentMonths) }
    val selections = remember { mutableStateListOf<LocalDate>() }
    val daysOfWeek = remember { daysOfWeek() }

    var isWeekMode by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val monthState = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
        )
        val weekState = rememberWeekCalendarState(
            startDate = startMonth.atStartOfMonth(),
            endDate = endMonth.atEndOfMonth(),
            firstVisibleWeekDate = currentDate,
            firstDayOfWeek = daysOfWeek.first(),
        )
        CalendarTitle(
            isWeekMode = isWeekMode,
            monthState = monthState,
            weekState = weekState,
            isAnimating = isAnimating,
        )
        AnimatedVisibility(visible = !isWeekMode) {
            HorizontalCalendar(
                state = monthState,
                monthHeader = { CalendarHeader(daysOfWeek = daysOfWeek) },
                dayContent = { day ->
                    CalendarDayView(
                        modifier = Modifier.padding(4.dp),
                        day = day,
                        isSelected = day.date.isEqual(currentDate.plusDays(3)),
//                        showIndicator = day.date.isEqual(currentDate.plusDays(1)),
                        showIndicator = true
                    ) {
                    }
                },
            )
        }
        AnimatedVisibility(visible = isWeekMode) {
            WeekCalendar(
                state = weekState,
                weekHeader = { CalendarHeader(daysOfWeek = daysOfWeek) },
                dayContent = { day ->
                    CalendarDayView(
                        modifier = Modifier.padding(4.dp),
                        day = day,
                        isSelected = day.date.isEqual(currentDate.plusDays(3)),
//                        showIndicator = day.date.isEqual(currentDate.plusDays(1)),
                        showIndicator = true

                    ) {
                    }
                },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        WeekModeToggle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isWeekMode = isWeekMode,
        ) { weekMode ->
            isAnimating = true
            isWeekMode = weekMode
            coroutineScope.launch {
                if (weekMode) {
                    val targetDate = monthState.firstVisibleMonth.weekDays.last().last().date
                    weekState.scrollToWeek(targetDate)
                    weekState.animateScrollToWeek(targetDate) // Trigger a layout pass for title update
                } else {
                    val targetMonth = weekState.firstVisibleWeek.days.first().date.yearMonth
                    monthState.scrollToMonth(targetMonth)
                    monthState.animateScrollToMonth(targetMonth) // Trigger a layout pass for title update
                }
                isAnimating = false
            }
        }
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/**
 * Returns the first visible month in a paged calendar **after** scrolling stops.
 *
 * @see [rememberFirstCompletelyVisibleMonth]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}

/**
 * Find first visible week in a paged week calendar **after** scrolling stops.
 */
@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

@Composable
private fun CalendarTitle(
    isWeekMode: Boolean,
    monthState: CalendarState,
    weekState: WeekCalendarState,
    isAnimating: Boolean,
) {
    val visibleMonth = rememberFirstVisibleMonthAfterScroll(monthState)
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(weekState)
    val visibleWeekMonth = visibleWeek.days.first().date.yearMonth
    // Track animation state to prevent updating the title too early before
    // the correct value is available (after the animation).
    val currentMonth = if (isWeekMode) {
        if (isAnimating) visibleMonth.yearMonth else visibleWeekMonth
    } else {
        if (isAnimating) visibleWeekMonth else visibleMonth.yearMonth
    }
    MonthAndWeekCalendarTitle(
        isWeekMode = isWeekMode,
        currentMonth = currentMonth,
        monthState = monthState,
        weekState = weekState,
    )
}


@Composable
fun MonthAndWeekCalendarTitle(
    isWeekMode: Boolean,
    currentMonth: YearMonth,
    monthState: CalendarState,
    weekState: WeekCalendarState,
) {
    val coroutineScope = rememberCoroutineScope()
    SimpleCalendarTitle(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
        currentMonth = currentMonth,
        goToPrevious = {
            coroutineScope.launch {
                if (isWeekMode) {
                    val targetDate = weekState.firstVisibleWeek.days.first().date.minusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                } else {
                    val targetMonth = monthState.firstVisibleMonth.yearMonth.previousMonth
                    monthState.animateScrollToMonth(targetMonth)
                }
            }
        },
        goToNext = {
            coroutineScope.launch {
                if (isWeekMode) {
                    val targetDate = weekState.firstVisibleWeek.days.last().date.plusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                } else {
                    val targetMonth = monthState.firstVisibleMonth.yearMonth.nextMonth
                    monthState.animateScrollToMonth(targetMonth)
                }
            }
        },
    )
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = Icons.Outlined.ChevronLeft,
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            icon = Icons.Outlined.ChevronRight,
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        imageVector = icon,
        contentDescription = contentDescription,
    )
}

@Composable
fun WeekModeToggle(
    modifier: Modifier,
    isWeekMode: Boolean,
    weekModeToggled: (isWeekMode: Boolean) -> Unit,
) {
    // We want the entire content to be clickable, not just the checkbox.
    Row(
        modifier = modifier
            .padding(10.dp)
            .clip(CustomTheme.shapes.small)
            .clickable { weekModeToggled(!isWeekMode) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        Checkbox(
            checked = isWeekMode,
            onCheckedChange = null, // Check is handled by parent.
            colors = CheckboxDefaults.colors(checkedColor = CustomTheme.colors.active),
        )
        Text(text = "Week mode")
    }
}