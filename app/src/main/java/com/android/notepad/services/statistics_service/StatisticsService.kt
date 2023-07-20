package com.android.notepad.services.statistics_service

import android.content.Context

class StatisticsService(val context: Context) {
    var appStatistics = AppStatistics(StatisticsStorage().getStatistics(context))

    fun saveStatistics() {
        StatisticsStorage().saveStatistics(appStatistics.statisticsData, context)
    }

    fun updateStatistics(newStatisticsData: StatisticsData) {
        (appStatistics.statisticsData.createdNotesCount.value > newStatisticsData.createdNotesCount.value) ?: return
        appStatistics.statisticsData = newStatisticsData
        saveStatistics()
    }
}