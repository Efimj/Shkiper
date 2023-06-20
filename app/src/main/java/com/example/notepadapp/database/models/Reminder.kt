package com.example.notepadapp.database.models

import android.content.Context
import com.example.notepadapp.R
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime

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
    var repeat: RepeatMode = RepeatMode.NONE
    var dateString: String = ""
    var timeString: String = ""

    var date: LocalDate
        get() = LocalDate.parse(dateString)
        set(value) {
            dateString = value.toString()
        }

    var time: LocalTime
        get() = LocalTime.parse(timeString)
        set(value) {
            timeString = value.toString()
        }
}