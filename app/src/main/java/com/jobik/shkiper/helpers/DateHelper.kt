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

            val newReminderDate = when (repeatMode) {
                RepeatMode.NONE -> {
                    if (oldReminderDate.isBefore(LocalDateTime.now())) {
                        oldReminderDate
                    } else {
                        oldReminderDate
                    }
                }

                RepeatMode.DAILY -> {
                    val updatedReminderDate = LocalDateTime.now()
                        .withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)

                    if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                        updatedReminderDate.plusDays(1)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.WEEKLY -> {
                    val updatedReminderDate = LocalDateTime.now()
                        .with(DayOfWeek.from(oldReminderDate.dayOfWeek))
                        .withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)

                    if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                        updatedReminderDate.plusDays(7)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.MONTHLY -> {
                    val updatedReminderDate = LocalDateTime.now()
                        .withDayOfMonth(oldReminderDate.dayOfMonth)
                        .withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)

                    if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                        updatedReminderDate.plusMonths(1)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.YEARLY -> {
                    val updatedReminderDate = LocalDateTime.now()
                        .withMonth(oldReminderDate.monthValue)
                        .withDayOfMonth(oldReminderDate.dayOfMonth)
                        .withHour(oldReminderDate.hour)
                        .withMinute(oldReminderDate.minute)

                    if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                        updatedReminderDate.plusYears(1)
                    } else {
                        updatedReminderDate
                    }
                }
            }
            return newReminderDate
        }
    }
}