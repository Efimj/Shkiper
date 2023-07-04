package com.example.notepadapp.app_handlers

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.notepadapp.SharedPreferencesKeys
import com.example.notepadapp.ui.theme.ColorThemes
import com.example.notepadapp.ui.theme.ExtendedColors
import com.example.notepadapp.ui.theme.UserTheme
import com.google.gson.Gson

class ThemePreferenceManager(val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ThemePreferencesName, Context.MODE_PRIVATE)
    private val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

    fun getSavedUserTheme(): UserTheme {
        val savedThemeString = sharedPreferences.getString(SharedPreferencesKeys.AppThemeKey, "")
        val currentTheme = if (savedThemeString.isNullOrEmpty()) {
            val isDarkTheme =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            UserTheme(isDarkTheme, ColorThemes.Default.name, ColorThemes.Default.name)
        } else {
            val gson = Gson()
            gson.fromJson(savedThemeString, UserTheme::class.java)
        }
        return currentTheme
    }

    fun saveTheme(userTheme: UserTheme) {
        setUiMode(userTheme.isDarkTheme)
        val gson = Gson()
        val themeString = gson.toJson(userTheme, UserTheme::class.java)
        sharedPreferences.edit().putString(SharedPreferencesKeys.AppThemeKey, themeString).apply()
    }

    private fun setUiMode(isDarkTheme: Boolean) {
        uiModeManager.nightMode = if (isDarkTheme) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}
