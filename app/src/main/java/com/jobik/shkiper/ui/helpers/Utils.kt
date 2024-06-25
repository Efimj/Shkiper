package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.*
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.helpers.DateHelper
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.Week
import kotlinx.coroutines.flow.filter
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

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

/**
 * To display month name.
 */
fun YearMonth.displayText(): String {
    return "${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${this.year}"
}

@Composable
fun rememberNextReminder(
    reminders: List<Reminder>,
    pointDate: LocalDateTime = LocalDateTime.now()
): Reminder? {
    val nextReminder = remember { mutableStateOf<Reminder?>(null) }

    LaunchedEffect(reminders) {
        nextReminder.value = DateHelper.sortReminders(reminders = reminders, pointDate = pointDate).firstOrNull()
    }

    return nextReminder.value
}

/**
 * To display month name.
 */
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.getDefault())
}

fun <T> splitIntoTriple(input: List<T>): Triple<List<T>, List<T>, List<T>> {
    val list1 = mutableListOf<T>()
    val list2 = mutableListOf<T>()
    val list3 = mutableListOf<T>()

    input.forEachIndexed { index, element ->
        when (index % 3) {
            0 -> list1.add(element)
            1 -> list2.add(element)
            2 -> list3.add(element)
        }
    }

    return Triple(list1, list2, list3)
}