package com.android.notepad.services.statistics_service

import android.content.Context
import com.android.notepad.SharedPreferencesKeys
import com.google.gson.Gson

class StatisticsStorage(context: Context) {
    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)

    fun updateStatistics(statistics: StatisticsData) {
        val json = gson.toJson(statistics)
        val editor = sharedPreferences.edit()
        editor.putString(SharedPreferencesKeys.Statistics, json)
        editor.apply()
    }

    fun getStatistics(): StatisticsData {
        val json = sharedPreferences.getString(SharedPreferencesKeys.Statistics, "")
        return if (json.isNullOrEmpty()) StatisticsData() else gson.fromJson(json, StatisticsData::class.java)
    }
}