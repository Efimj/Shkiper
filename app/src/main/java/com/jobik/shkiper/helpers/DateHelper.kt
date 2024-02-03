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
            val updatedReminderDate = LocalDateTime.now().let {
                it.withNano(oldReminderDate.nano).withSecond(oldReminderDate.second)

                when (repeatMode) {
                    RepeatMode.NONE -> oldReminderDate
                    RepeatMode.DAILY -> it.withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)

                    RepeatMode.WEEKLY -> it.with(DayOfWeek.from(oldReminderDate.dayOfWeek))
                        .withHour(oldReminderDate.hour).withMinute(oldReminderDate.minute)

                    RepeatMode.MONTHLY -> it.withDayOfMonth(oldReminderDate.dayOfMonth)
                        .withHour(oldReminderDate.hour).withMinute(oldReminderDate.minute)

                    RepeatMode.YEARLY -> it.withMonth(oldReminderDate.monthValue)
                        .withDayOfMonth(oldReminderDate.dayOfMonth).withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)
                }
            }

            val isDateEqualOrBefore = updatedReminderDate.isBefore(LocalDateTime.now()) || LocalDateTime.now()
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
    }
}