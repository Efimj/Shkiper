package com.example.notepadapp.app_handlers

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme

class ThemePreferenceManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "AppSettingPreferences"
        private const val KEY_THEME = "theme_mode"
    }

    fun getSavedTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_THEME, false)
    }

    fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME, isDarkTheme).apply()
    }
}
