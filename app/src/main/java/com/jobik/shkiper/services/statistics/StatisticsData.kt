package com.jobik.shkiper.services.statistics

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.NumberHelper
import java.time.LocalDate

@Keep
abstract class Statistics {
    abstract fun increment()

}

enum class StatisticsType {
    Long,
    Boolean,
    Date
}

@Keep
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

@Keep
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

@Keep
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

@Keep
data class StatisticsItem(
    @StringRes
    val title: Int,
    @StringRes
    val description: Int,
    private val statistics: Statistics
) {
    @Composable
    fun getStringValue(): String {
        return when (statistics) {
            is LongStatistics -> statistics.value.toString()
            is BooleanStatistics -> statistics.value.toString()
            is DateStatistics -> statistics.value.toString()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }

    fun increment() {
        when (statistics) {
            is LongStatistics -> statistics.increment()
            is BooleanStatistics -> statistics.increment()
            is DateStatistics -> statistics.increment()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }

    val type: StatisticsType
        get() {
            return when (statistics) {
                is LongStatistics -> StatisticsType.Long
                is BooleanStatistics -> StatisticsType.Boolean
                is DateStatistics -> StatisticsType.Date
                else -> throw UnsupportedOperationException("Unsupported type")
            }
        }
}

@Keep
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

@Keep
data class AppStatistics(
    var statisticsData: StatisticsData = StatisticsData(),

    val fistOpenDate: StatisticsItem = StatisticsItem(
        R.string.FirstAppOpenDate,
        R.string.FirstAppOpenDateDescription,
        statisticsData.firstOpenDate
    ),
    val openAppCount: StatisticsItem = StatisticsItem(
        R.string.Thinker,
        R.string.ThinkerDescription,
        statisticsData.openAppCount
    ),
    val createdNotesCount: StatisticsItem = StatisticsItem(
        R.string.Creator,
        R.string.CreatorDescription,
        statisticsData.createdNotesCount
    ),
    val createdRemindersCount: StatisticsItem = StatisticsItem(
        R.string.Eternal,
        R.string.EternalDescription,
        statisticsData.createdRemindersCount
    ),
    val notificationCount: StatisticsItem = StatisticsItem(
        R.string.Remembered,
        R.string.RememberedDescription,
        statisticsData.notificationCount
    ),
    val isPioneer: StatisticsItem = StatisticsItem(
        R.string.Pioneer,
        R.string.PioneerDescription,
        statisticsData.isPioneer
    ),
    val truthSeeker: StatisticsItem = StatisticsItem(
        R.string.TruthSeeker,
        R.string.TruthSeekerDescription,
        statisticsData.truthSeeker
    ),
    val noteDeletedCount: StatisticsItem = StatisticsItem(
        R.string.NoteDestroyer,
        R.string.NoteDestroyerDescription,
        statisticsData.noteDeletedCount
    ),
) {
    @Keep
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

    @Keep
    @Composable
    fun getStatisticsText(): String {
        var string = "${stringResource(R.string.MyStatsInShkiper)}\n\n"
        getStatisticsPreviews().forEach { statistics ->
            string = string.plus(
                "${stringResource(statistics.title)}: ${statistics.getStringValue()}"
            ).plus("\n")
        }
        return string
    }
}