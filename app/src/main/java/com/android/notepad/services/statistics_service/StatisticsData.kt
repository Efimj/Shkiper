package com.android.notepad.services.statistics_service

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.notepad.R
import com.android.notepad.helpers.NumberHelper

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
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }

    fun increment() {
        when (statistics) {
            is LongStatistics -> (statistics as LongStatistics).increment()
            is BooleanStatistics -> (statistics as BooleanStatistics).increment()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }

    fun isMore(item: StatisticsItem) {
        when (statistics) {
            is LongStatistics -> {
                if(item.statistics is LongStatistics)
                (statistics as LongStatistics).increment()
            }

            is BooleanStatistics -> (statistics as BooleanStatistics).increment()
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }
}

data class StatisticsData(
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
    val openAppCount: StatisticsItem = StatisticsItem(
        R.drawable.onboarding_reminder,
        R.string.Thinker,
        R.string.ThinkerDescription,
        statisticsData.openAppCount
    ),
    val createdNotesCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_splash,
        R.string.Creator,
        R.string.CreatorDescription,
        statisticsData.createdNotesCount
    ),
    val createdRemindersCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_notification,
        R.string.Eternal,
        R.string.EternalDescription,
        statisticsData.createdRemindersCount
    ),
    val notificationCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_splash,
        R.string.Remembered,
        R.string.RememberedDescription,
        statisticsData.notificationCount
    ),
    val isPioneer: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.Pioneer,
        R.string.PioneerDescription,
        statisticsData.isPioneer
    ),
    val truthSeeker: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.TruthSeeker,
        R.string.TruthSeekerDescription,
        statisticsData.truthSeeker
    ),
    val noteDeletedCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
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
}