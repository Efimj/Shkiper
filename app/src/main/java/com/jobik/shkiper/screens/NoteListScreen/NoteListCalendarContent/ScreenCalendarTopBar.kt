package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.components.fields.DaysOfWeekTitle
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.helpers.WindowWidthSizeClass
import com.jobik.shkiper.ui.helpers.isWidth
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(CustomTheme.colors.secondaryBackground)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CustomTheme.colors.secondaryBackground,
            text = "Events",
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
                    onClick = { }
                ),
            )
        )

        val isCompactWidthScreen = isWidth(sizeClass = WindowWidthSizeClass.Compact)
        val weekState = rememberWeekCalendarState(
            startDate = startMonth.atStartOfMonth(),
            endDate = endMonth.atEndOfMonth(),
            firstVisibleWeekDate = currentDate,
            firstDayOfWeek = daysOfWeek.first(),
        )

        WeekCalendar(
            calendarScrollPaged = isCompactWidthScreen,
            state = weekState,
            weekHeader = { DaysOfWeekTitle(daysOfWeek = daysOfWeek) },
            contentPadding = PaddingValues(horizontal = 10.dp),
            dayContent = { day ->
                val hasEvent = remember { mutableStateOf(false) }

                LaunchedEffect(viewModel.screenState.value.reminders) {
                    hasEvent.value = viewModel.screenState.value.reminders.any {
                        DateHelper.nextDateWithRepeating(
                            notificationDate = LocalDateTime.of(it.date, it.time),
                            repeatMode = it.repeat,
                            startingPoint = LocalDateTime.of(day.date, LocalTime.now())
                        ).toLocalDate() == day.date
                    }
                }

                CalendarDayView(
                    modifier =
                    if (isCompactWidthScreen)
                        Modifier.padding(4.dp)
                    else Modifier
                        .height(60.dp)
                        .padding(4.dp),
                    day = day,
                    isSelected = viewModel.screenState.value.selectedDateRange.first == day.date || viewModel.screenState.value.selectedDateRange.second == day.date,
                    showIndicator = hasEvent.value,
                ) {
                    viewModel.selectDate(date = it.date)
                }
            },
        )
    }
}