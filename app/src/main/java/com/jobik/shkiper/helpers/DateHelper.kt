package com.jobik.shkiper.helpers

import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DateHelper {
    companion object {
        fun isFutureDateTime(
            date: LocalDate,
            time: LocalTime,
            currentDate: LocalDate = LocalDate.now(),
            currentTime: LocalTime = LocalTime.now()
        ): Boolean {
            return if (date.isAfter(currentDate)) {
                true
            } else if (date.isBefore(currentDate)) {
                false
            } else {
                time.isAfter(currentTime)
            }
        }

        fun isFutureDateTime(
            dateTime: LocalDateTime,
            currentDateTime: LocalDateTime = LocalDateTime.now(),
        ): Boolean {
            return dateTime.isAfter(currentDateTime)
        }

        fun getLocalizedDate(date: LocalDate): String {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())
            return date.format(formatter)
        }

        fun getLocalizedDate(date: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())
            return date.format(formatter)
        }

        fun nextDateWithRepeating(
            notificationDate: LocalDateTime,
            repeatMode: RepeatMode,
            startingPoint: LocalDateTime = LocalDateTime.now()
        ): LocalDateTime {
            // get current date with old values
            val updatedReminderDate = startingPoint.let {
                it.withNano(notificationDate.nano).withSecond(notificationDate.second)

                when (repeatMode) {
                    RepeatMode.NONE -> notificationDate
                    RepeatMode.DAILY -> it.withHour(notificationDate.hour)
                        .withMinute(notificationDate.minute)

                    RepeatMode.WEEKLY -> it.with(DayOfWeek.from(notificationDate.dayOfWeek))
                        .withHour(notificationDate.hour).withMinute(notificationDate.minute)

                    RepeatMode.MONTHLY -> it.withDayOfMonth(notificationDate.dayOfMonth)
                        .withHour(notificationDate.hour).withMinute(notificationDate.minute)

                    RepeatMode.YEARLY -> it.withMonth(notificationDate.monthValue)
                        .withDayOfMonth(notificationDate.dayOfMonth).withHour(notificationDate.hour)
                        .withMinute(notificationDate.minute)
                }
            }

            val isDateEqualOrBefore = updatedReminderDate.isBefore(startingPoint) || startingPoint
                .isEqual(updatedReminderDate)

            // update date with adding repeating value
            return if (isDateEqualOrBefore) {
                when (repeatMode) {
                    RepeatMode.DAILY -> updatedReminderDate.plusDays(1)
                    RepeatMode.WEEKLY -> updatedReminderDate.plusDays(7)
                    RepeatMode.MONTHLY -> updatedReminderDate.plusMonths(1)
                    RepeatMode.YEARLY -> updatedReminderDate.plusYears(1)
                    else -> updatedReminderDate
                }
            } else {
                updatedReminderDate
            }
        }

        fun sortReminders(reminders: List<Reminder>, pointDate: LocalDateTime = LocalDateTime.now()): List<Reminder> {
            val comparator = compareBy<Reminder> {
                nextDateWithRepeating(
                    LocalDateTime.of(it.date, it.time),
                    it.repeat
                )
            }
            return reminders.sortedWith(comparator).sortedBy {
                if (nextDateWithRepeating(LocalDateTime.of(it.date, it.time), it.repeat).isBefore(pointDate)) {
                    1
                } else {
                    0
                }
            }
        }

        fun isLocalDateInRange(date: LocalDate, range: Pair<LocalDate, LocalDate>): Boolean {
            val (start, end) = if (range.first.isAfter(range.second)) Pair(range.second, range.first) else range
            return date in start..end
        }
    }
}