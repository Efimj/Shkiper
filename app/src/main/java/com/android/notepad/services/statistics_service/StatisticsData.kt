package com.android.notepad.services.statistics_service

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.android.notepad.R

data class StatisticsItem(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int,
    private var _progress: ULong = 0u,
) {
    private fun setValue(value: ULong, current: ULong): ULong {
        return if (value == ULong.MAX_VALUE) current else value
    }

    var progress: ULong
        get() = _progress
        set(value) {
            _progress = setValue(value, _progress)
        }
}

data class StatisticsData(
    val openAppCount: StatisticsItem = StatisticsItem(
        R.drawable.onboarding_reminder,
        R.string.Thinker,
        R.string.ThinkerDescription
    ),
    val createdNotesCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.Creator,
        R.string.CreatorDescription
    ),
    val createdRemindersCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_notification,
        R.string.Eternal,
        R.string.EternalDescription
    ),
    val notificationCount: StatisticsItem = StatisticsItem(
        R.drawable.ic_splash,
        R.string.Remembered,
        R.string.RememberedDescription
    ),
    val isPioneer: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.Pioneer,
        R.string.PioneerDescription
    ),
    val truthSeeker: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.TruthSeeker,
        R.string.TruthSeekerDescription
    ),
    val noteDestroyer: StatisticsItem = StatisticsItem(
        R.drawable.ic_launcher_background,
        R.string.NoteDestroyer,
        R.string.NoteDestroyerDescription
    ),
)