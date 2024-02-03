package com.jobik.shkiper.helpers

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
            date: LocalDate, time: LocalTime, repeatMode: RepeatMode
        ): LocalDateTime {
            val oldReminderDate = LocalDateTime.of(date, time)

            // get current date with old values
            val updatedReminderDate = when (repeatMode) {
                RepeatMode.NONE -> oldReminderDate
                RepeatMode.DAILY -> LocalDateTime.now().withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)

                RepeatMode.WEEKLY -> LocalDateTime.now().with(DayOfWeek.from(oldReminderDate.dayOfWeek))
                    .withHour(oldReminderDate.hour).withMinute(oldReminderDate.minute)

                RepeatMode.MONTHLY -> LocalDateTime.now().withDayOfMonth(oldReminderDate.dayOfMonth)
                    .withHour(oldReminderDate.hour).withMinute(oldReminderDate.minute)

                RepeatMode.YEARLY -> LocalDateTime.now().withMonth(oldReminderDate.monthValue)
                    .withDayOfMonth(oldReminderDate.dayOfMonth).withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)
            }

            // update date with adding repeating value
            return if (updatedReminderDate.isBefore(LocalDateTime.now())) {
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
    }
}