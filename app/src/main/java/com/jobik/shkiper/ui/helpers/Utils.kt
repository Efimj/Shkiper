package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.*
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.helpers.DateHelper
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.Week
import kotlinx.coroutines.flow.filter
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
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
    noteId: ObjectId,
    pointDate: LocalDateTime = LocalDateTime.now()
): Reminder? {
    val nextReminder = remember { mutableStateOf<Reminder?>(null) }

    LaunchedEffect(reminders) {
        val noteReminders = reminders.filter { it.noteId == noteId }
        nextReminder.value = DateHelper.sortReminders(reminders = noteReminders, pointDate = pointDate).firstOrNull()
    }
    return nextReminder.value
}