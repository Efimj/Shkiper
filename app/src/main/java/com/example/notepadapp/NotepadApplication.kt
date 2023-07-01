package com.example.notepadapp

import android.app.Application
import android.content.Context
import com.example.notepadapp.helpers.localization.LocaleHelper
import com.example.notepadapp.helpers.localization.Localization
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotepadApplication : Application() {
    override fun attachBaseContext(base: Context) {
        val currentLocalization = LocaleHelper.getSavedLocalization(base) ?: LocaleHelper.getDeviceLocalization(base)
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
