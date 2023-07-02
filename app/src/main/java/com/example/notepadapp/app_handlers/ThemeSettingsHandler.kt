package com.example.notepadapp.app_handlers

import android.app.UiModeManager
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
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiModeManager.nightMode = if(isDarkTheme) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        sharedPreferences.edit().putBoolean(SharedPreferencesKeys.AppThemeKey, isDarkTheme).apply()
    }
}
