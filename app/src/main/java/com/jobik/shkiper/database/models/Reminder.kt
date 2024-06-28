package com.jobik.shkiper.database.models

import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.CustomThemeColors
import com.jobik.shkiper.ui.theme.CustomThemeStyle.M3Green
import com.jobik.shkiper.ui.theme.CustomThemeStyle.MaterialDynamicColors
import com.jobik.shkiper.ui.theme.CustomThemeStyle.PastelBlue
import com.jobik.shkiper.ui.theme.CustomThemeStyle.PastelOrange
import com.jobik.shkiper.ui.theme.CustomThemeStyle.PastelPurple
import com.jobik.shkiper.ui.theme.CustomThemeStyle.PastelRed
import com.jobik.shkiper.ui.theme.getThemeColors
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

@Keep
enum class RepeatMode {
    NONE, DAILY, WEEKLY, MONTHLY, YEARLY;

    fun getLocalizedValue(context: Context): String {
        val string: String = when (name) {
            NONE.name -> context.getString(R.string.none)

            DAILY.name -> context.getString(R.string.daily)

            WEEKLY.name -> context.getString(R.string.weekly)

            MONTHLY.name -> context.getString(R.string.monthly)

            YEARLY.name -> context.getString(R.string.yearly)

            else -> ""
        }
        return string
    }
}

@Keep
enum class NotificationIcon {
    EVENT,
    FLAG,
    WARNING,
    WORK,
    PILL,
    CELEBRATION,
    FITNESS,
    TRAVEL;

    @DrawableRes
    fun getDrawable(): Int {
        @DrawableRes val id: Int = when (name) {
            EVENT.name -> R.drawable.notification_ic_event
            WORK.name -> R.drawable.notification_ic_work
            FLAG.name -> R.drawable.notification_ic_flag
            WARNING.name -> R.drawable.notification_ic_warning
            PILL.name -> R.drawable.notification_ic_pill
            CELEBRATION.name -> R.drawable.notification_ic_celebration
            FITNESS.name -> R.drawable.notification_ic_fitness
            TRAVEL.name -> R.drawable.notification_ic_travel

            else -> throw NotFoundException("NotificationIcon cant find with: $name")
        }
        return id
    }
}

@Keep
enum class NotificationColor {
    MATERIAL,
    RED,
    ORANGE,
    GREEN,
    BLUE,
    PURPLE;

    fun getColor(context: Context): Color {
        val color: CustomThemeColors = when (name) {
            MATERIAL.name -> getThemeColors(
                context = context,
                style = MaterialDynamicColors
            )

            RED.name -> getThemeColors(
                context = context,
                style = PastelRed
            )

            ORANGE.name -> getThemeColors(
                context = context,
                style = PastelOrange
            )

            GREEN.name -> getThemeColors(
                context = context,
                style = M3Green
            )

            BLUE.name -> getThemeColors(
                context = context,
                style = PastelBlue
            )

            PURPLE.name -> getThemeColors(
                context = context,
                style = PastelPurple
            )

            else -> throw NotFoundException("NotificationColor cant find with: $name")
        }
        return color.primary
    }
}

class Reminder : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()

    @Index
    var noteId: ObjectId = ObjectId.invoke()
    var repeatString: String = RepeatMode.NONE.name
    var iconString: String = NotificationIcon.EVENT.name
    var colorString: String = NotificationColor.MATERIAL.name
    var dateString: String = ""
    var timeString: String = ""

    var repeat: RepeatMode
        get() = RepeatMode.valueOf(repeatString)
        set(value) {
            repeatString = value.name
        }

    var icon: NotificationIcon
        get() = NotificationIcon.valueOf(iconString)
        set(value) {
            iconString = value.name
        }

    var color: NotificationColor
        get() = NotificationColor.valueOf(colorString)
        set(value) {
            colorString = value.name
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