package com.jobik.shkiper.services.statistics_service

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.NumberHelper
import java.time.LocalDate

abstract class Statistics {
    abstract fun increment()

}

data class LongStatistics(
    private var _value: ULong
) : Statistics() {
    var value: ULong
        get() = _value
        set(newValue) {
            _value = newValue
        }

    override fun increment() {
        _value++
    }
}

data class BooleanStatistics(
    private var _value: Boolean
) : Statistics() {
    var value: Boolean
        get() = _value
        set(newValue) {
            _value = newValue
        }

    override fun increment() {
        _value = true
    }
}

data class DateStatistics(
    private var _value: String?
) : Statistics() {
    var value: LocalDate?
        get() {
            return if (_value == null) null else LocalDate.parse(_value)
        }
        set(newValue) {
            _value = newValue.toString()
        }

    override fun increment() {
        if (_value == null)
            _value = LocalDate.now().toString()
    }
}

data class StatisticsItem(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int,
    private var statistics: Statistics
) {
    @Composable
    fun getStringValue(): String {
        return when (statistics) {
            is LongStatistics -> NumberHelper().formatNumber((statistics as LongStatistics).value)
            is BooleanStatistics -> stringResource(if ((statistics as BooleanStatistics).value) R.string.DoneEmoji else R.string.NotDoneEmoji)
            is DateStatistics -> (statistics as DateStatistics).value.toString()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }

    fun increment() {
        when (statistics) {
            is LongStatistics -> (statistics as LongStatistics).increment()
            is BooleanStatistics -> (statistics as BooleanStatistics).increment()
            is DateStatistics -> (statistics as DateStatistics).increment()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }
}

data class StatisticsData(
    val firstOpenDate: DateStatistics = DateStatistics(null),
    val openAppCount: LongStatistics = LongStatistics(0u),
    val createdNotesCount: LongStatistics = LongStatistics(0u),
    val createdRemindersCount: LongStatistics = LongStatistics(0u),
    val notificationCount: LongStatistics = LongStatistics(0u),
    val isPioneer: BooleanStatistics = BooleanStatistics(false),
    val truthSeeker: BooleanStatistics = BooleanStatistics(false),
    val noteDeletedCount: LongStatistics = LongStatistics(0u),
)

data class AppStatistics(
    var statisticsData: StatisticsData = StatisticsData(),

    val fistOpenDate: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_first_open_date,
        R.string.FirstAppOpenDate,
        R.string.FirstAppOpenDateDescription,
        statisticsData.firstOpenDate
    ),
    val openAppCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_open_app_count,
        R.string.Thinker,
        R.string.ThinkerDescription,
        statisticsData.openAppCount
    ),
    val createdNotesCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_created_notes,
        R.string.Creator,
        R.string.CreatorDescription,
        statisticsData.createdNotesCount
    ),
    val createdRemindersCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_created_reminders_count,
        R.string.Eternal,
        R.string.EternalDescription,
        statisticsData.createdRemindersCount
    ),
    val notificationCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_notifications_count,
        R.string.Remembered,
        R.string.RememberedDescription,
        statisticsData.notificationCount
    ),
    val isPioneer: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_pioneer,
        R.string.Pioneer,
        R.string.PioneerDescription,
        statisticsData.isPioneer
    ),
    val truthSeeker: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_truth_seeker,
        R.string.TruthSeeker,
        R.string.TruthSeekerDescription,
        statisticsData.truthSeeker
    ),
    val noteDeletedCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_achievements_deleted_notes,
        R.string.NoteDestroyer,
        R.string.NoteDestroyerDescription,
        statisticsData.noteDeletedCount
    ),
) {
    fun getStatisticsPreviews(): List<StatisticsItem> {
        var previewsList = emptyList<StatisticsItem>()
        for (property in this.javaClass.declaredFields) {
            property.isAccessible = true
            val value = property.get(this)
            if (value is StatisticsItem) {
                previewsList = previewsList.plus(value)
            }
        }
        return previewsList
    }

    @Composable
    fun getStatisticsText(): String {
        var string = ""
        getStatisticsPreviews().forEach { statistics ->
            string = string.plus(
                "${stringResource(statistics.title)}: ${statistics.getStringValue()}"
            ).plus("\n")
        }
        return string
    }
}