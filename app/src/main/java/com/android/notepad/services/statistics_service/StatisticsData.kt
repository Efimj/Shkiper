package com.android.notepad.services.statistics_service

data class StatisticsData(
    private var _openAppCount: ULong = 0u,
    private var _countCreatedNotes: ULong = 0u,
    private var _countCreatedReminders: ULong = 0u,
) {
    private fun setValue(value: ULong, current: ULong): ULong {
        return if (value == ULong.MAX_VALUE) current else value
    }

    var openAppCount: ULong
        get() = _openAppCount
        set(value) {
            setValue(value, _openAppCount)
        }

    var countCreatedNotes: ULong
        get() = _countCreatedNotes
        set(value) {
            setValue(value, _countCreatedNotes)
        }

    var countCreatedReminders: ULong
        get() = _countCreatedReminders
        set(value) {
            setValue(value, _countCreatedReminders)
        }
}
