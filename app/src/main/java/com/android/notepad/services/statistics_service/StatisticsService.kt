package com.android.notepad.services.statistics_service

import android.content.Context

class StatisticsService {
    fun incrementCreatedNotesCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                createdNotesCount.progress++
            }
            saveStatistics(statistics, context)
        }
    }

    fun incrementCreatedRemindersCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                createdRemindersCount.progress++
            }
            saveStatistics(statistics, context)
        }
    }

    fun incrementOpenAppCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                openAppCount.progress++
            }
            saveStatistics(statistics, context)
        }
    }

    fun incrementNotificationCount(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                notificationCount.progress++
            }
            saveStatistics(statistics, context)
        }
    }

    fun setPioneerAchievement(context: Context) {
        StatisticsStorage().apply {
            val statistics = getStatistics(context).apply {
                isPioneer.progress++
            }
            saveStatistics(statistics, context)
        }
    }
}