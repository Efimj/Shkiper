package com.jobik.shkiper.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.*
import com.google.gson.Gson
import com.jobik.shkiper.SharedPreferencesKeys.ApplicationStorageName
import com.jobik.shkiper.SharedPreferencesKeys.ApplicationUiMode
import com.jobik.shkiper.ui.theme.*

object ThemeUtil {
    private var _isDarkMode: MutableState<Boolean?> = mutableStateOf(null)
    var isDarkMode: MutableState<Boolean?>
        get() = _isDarkMode
        private set(value) {
            _isDarkMode = value
        }

    private var _themeStyle: MutableState<CustomThemeStyle?> = mutableStateOf(null)
    var themeStyle: MutableState<CustomThemeStyle?>
        get() = _themeStyle
        private set(value) {
            _themeStyle = value
        }

    fun getColors(isDark: Boolean? = null, style: CustomThemeStyle? = null): CustomThemeColors {
        val currentStyle = style ?: (themeStyle.value ?: CustomThemeStyle.DarkPurple)
        val currentIsDark = isDark ?: isDarkMode.value ?: true

        return currentStyle.getColors(currentIsDark)
    }

    data class StoredUiTheme(val isDarkTheme: Boolean, val themeStyle: CustomThemeStyle)

    fun restoreSavedTheme(context: Context) {
        val store = context.getSharedPreferences(ApplicationStorageName, Context.MODE_PRIVATE)
        val savedThemeString = store.getString(ApplicationUiMode, "")
        if (savedThemeString.isNullOrEmpty()) {
            setDefaultsStyles(context)
        } else {
            try {
                val gson = Gson()
                val theme = gson.fromJson(savedThemeString, StoredUiTheme::class.java)
                isDarkMode.value = theme.isDarkTheme
                themeStyle.value = theme.themeStyle
            } catch (e: Exception) {
                setDefaultsStyles(context)
            }
        }
    }

    private fun setDefaultsStyles(context: Context) {
        isDarkMode.value =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        themeStyle.value = CustomThemeStyle.DarkPurple
    }

    fun saveThemeMode(context: Context, mode: Boolean, newThemeStyle: CustomThemeStyle) {
        val storedUiTheme = StoredUiTheme(isDarkTheme = mode, themeStyle = newThemeStyle)
        val gson = Gson()
        val storedUiThemeString = gson.toJson(storedUiTheme, StoredUiTheme::class.java)
        val store = context.getSharedPreferences(ApplicationStorageName, Context.MODE_PRIVATE)
        store.edit().putString(ApplicationUiMode, storedUiThemeString.toString()).apply()
        isDarkMode.value = mode
        themeStyle.value = newThemeStyle
    }
}
