package com.example.notepadapp.app_handlers

import android.content.Context
import android.content.res.Configuration
import com.example.notepadapp.SharedPreferencesKeys

class ThemePreferenceManager(val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ThemePreferencesName, Context.MODE_PRIVATE)

    fun getSavedTheme(): Boolean {
        return sharedPreferences.getBoolean(
            SharedPreferencesKeys.AppThemeKey,
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        )
    }

    fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKeys.AppThemeKey, isDarkTheme).apply()
    }
}
