package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.Week
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import org.mongodb.kbson.ObjectId
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

/**
 * To display month name.
 */
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.getDefault())
}

/**
 * Sets the current color, and with the dispose sets the previous one.
 */
@Composable
fun UpdateStatusBarColor(
    current: Color, previous: Color = CustomTheme.colors.mainBackground, delayMs: Long = 500L
) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        delay(delayMs)
        systemUiController.setStatusBarColor(
            color = current
        )
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = current
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = previous
            )
        }
    }
}
