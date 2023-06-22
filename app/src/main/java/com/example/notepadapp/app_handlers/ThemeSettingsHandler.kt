package com.example.notepadapp.app_handlers

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.notepadapp.SharedPreferencesKeys

class ThemePreferenceManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ThemePreferencesName, Context.MODE_PRIVATE)

    fun getSavedTheme(): Boolean {
        return sharedPreferences.getBoolean(SharedPreferencesKeys.AppThemeKey, false)
    }

    fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(SharedPreferencesKeys.AppThemeKey, isDarkTheme).apply()
    }
}
