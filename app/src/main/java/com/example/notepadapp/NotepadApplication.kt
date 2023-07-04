package com.example.notepadapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.*
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.helpers.localization.LocaleHelper
import com.example.notepadapp.helpers.localization.Localization
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotepadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setAppTheme(applicationContext)
    }

    private fun setAppTheme(context: Context) {
        setDefaultNightMode(if (ThemePreferenceManager(context).getSavedUserTheme().isDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

    override fun attachBaseContext(base: Context) {
        val currentLocalization = LocaleHelper.getSavedLocalization(base) ?: LocaleHelper.getDeviceLocalization()
        super.attachBaseContext(LocaleHelper.setLocale(base, currentLocalization ?: Localization.EN))
    }

    companion object {
        private var _currentLanguage = Localization.EN
        var currentLanguage: Localization
            get() {
                return _currentLanguage
            }
            set(value) {
                _currentLanguage = value
            }
    }
}
