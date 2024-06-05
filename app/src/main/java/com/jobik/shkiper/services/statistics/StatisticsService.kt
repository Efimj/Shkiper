package com.jobik.shkiper.services.statistics

import android.content.Context

class StatisticsService(val context: Context) {
    var appStatistics = AppStatistics(StatisticsStorage().getStatistics(context))

    fun saveStatistics() {
        StatisticsStorage().saveStatistics(appStatistics.statisticsData, context)
    }

    fun updateStatistics(newStatisticsData: StatisticsData) {
        for ((index, property) in appStatistics.statisticsData.javaClass.declaredFields.withIndex()) {
            property.isAccessible = true
            val value = property.get(appStatistics.statisticsData)
            val newStatisticsValue = newStatisticsData.javaClass.declaredFields[index]
            newStatisticsValue.isAccessible = true
            val newValue = property.get(newStatisticsData)

            if (value is LongStatistics && newValue is LongStatistics)
                if (newValue.value > value.value) value.value = newValue.value
            if (value is BooleanStatistics && newValue is BooleanStatistics)
                if (newValue.value > value.value) value.value = newValue.value
        }
        saveStatistics()
    }
}