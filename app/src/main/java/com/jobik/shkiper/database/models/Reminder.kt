package com.jobik.shkiper.database.models

import android.content.Context
import com.jobik.shkiper.R
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

enum class RepeatMode {
    NONE, DAILY, WEEKLY, MONTHLY, YEARLY;

    fun getLocalizedValue(context: Context): String {
        val string: String = when (name) {
            RepeatMode.NONE.name -> context.getString(R.string.none)

            RepeatMode.DAILY.name -> context.getString(R.string.daily)

            RepeatMode.WEEKLY.name -> context.getString(R.string.weekly)

            RepeatMode.MONTHLY.name -> context.getString(R.string.monthly)

            RepeatMode.YEARLY.name -> context.getString(R.string.yearly)

            else -> ""
        }
        return string
    }
}

class Reminder : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()

    @Index
    var noteId: ObjectId = ObjectId.invoke()
    var repeatString: String = RepeatMode.NONE.name
    var dateString: String = ""
    var timeString: String = ""

    var repeat: RepeatMode
        get() = RepeatMode.valueOf(repeatString)
        set(value) {
            repeatString = value.name
        }

    var date: LocalDate
        get() {
            return try {
                LocalDate.parse(dateString)
            } catch (e: DateTimeParseException) {
                LocalDate.now()
            }
        }
        set(value) {
            dateString = value.toString()
        }

    var time: LocalTime
        get() {
            return try {
                LocalTime.parse(timeString)
            } catch (e: DateTimeParseException) {
                LocalTime.now()
            }
        }
        set(value) {
            timeString = value.toString()
        }
}