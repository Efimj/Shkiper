package com.example.notepadapp.helpers

import java.time.LocalDate
import java.time.LocalTime
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

        fun getLocalizedDate(date: LocalDate): String {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())
            return date.format(formatter)
        }
    }
}