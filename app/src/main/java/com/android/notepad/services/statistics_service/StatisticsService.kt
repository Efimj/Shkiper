package com.android.notepad.services.statistics_service

import android.content.Context

class StatisticsService {
    fun incrementCreatedNotesCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                countCreatedNotes++
            }
            saveStatistics(statistics, context)
        }
    }

    fun incrementCreatedRemindersCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                countCreatedReminders++
            }
            saveStatistics(statistics, context)
        }
    }

    fun incrementOpenAppCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                openAppCount++
            }
            saveStatistics(statistics, context)
        }
    }
}