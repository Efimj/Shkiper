package com.jobik.shkiper.util.settings

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.jobik.shkiper.SharedPreferencesKeys.ApplicationSettings
import com.jobik.shkiper.SharedPreferencesKeys.ApplicationStorageName
import com.jobik.shkiper.SharedPreferencesKeys.ApplicationUiMode

object SettingsManager {
    private var _settings: MutableState<SettingsState?> = mutableStateOf(null)
    var settings: MutableState<SettingsState?>
        get() = _settings
        private set(value) {
            _settings = value
        }

    fun init(context: Context) {
        settings.value = restore(context = context)
    }

    fun update(context: Context, settingsState: SettingsState) {
        val storedUiThemeString = Gson().toJson(settingsState, SettingsState::class.java)
        val store = context.getSharedPreferences(ApplicationStorageName, Context.MODE_PRIVATE)
        store.edit().putString(ApplicationUiMode, storedUiThemeString.toString()).apply()
    }

    private fun restore(context: Context): SettingsState {
        val store = context.getSharedPreferences(ApplicationStorageName, Context.MODE_PRIVATE)
        val savedSettings = store.getString(ApplicationSettings, "")
        return if (savedSettings.isNullOrEmpty()) {
            SettingsState()
        } else {
            try {
                Gson().fromJson(savedSettings, SettingsState::class.java)
            } catch (e: Exception) {
                SettingsState()
            }
        }
    }
}