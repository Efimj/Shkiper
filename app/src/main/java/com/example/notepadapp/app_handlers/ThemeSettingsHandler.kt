package com.example.notepadapp.app_handlers

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme

//object ThemeSettingsHandler {
//    private const val PREFS_NAME = "AppSettingPreferences"
//    private const val THEME_KEY = "theme_mode"
//
//    fun switchTheme(context: Context, isDarkTheme: Boolean) {
//        saveTheme(context, isDarkTheme)
//    }
//
//    private fun saveTheme(context: Context, isDarkTheme: Boolean) {
//        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        with(prefs.edit()) {
//            putBoolean(THEME_KEY, isDarkTheme)
//            apply()
//        }
//    }
//
//    fun loadSavedTheme(context: Context): Boolean {
//        // Загружаем сохраненный режим темы из SharedPreferences или другого хранилища
//        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        return prefs.getBoolean(THEME_KEY, false)
//    }
//}

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
