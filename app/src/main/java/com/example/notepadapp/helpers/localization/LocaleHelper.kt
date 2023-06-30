package com.example.notepadapp.helpers.localization

import android.content.Context
import com.example.notepadapp.NotepadApplication
import com.example.notepadapp.SharedPreferencesKeys
import java.util.*

object LocaleHelper {
    fun setLocale(context: Context, localization: Localization): Context? {
        NotepadApplication.currentLanguage = localization
        saveLocalization(context, localization)
        return updateResources(context, localization)
    }

    private fun saveLocalization(context: Context, localization: Localization) {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(SharedPreferencesKeys.Localization, localization.name).apply()
    }

    fun getSavedLocalization(context: Context): Localization? {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        val notSelected = "NotSelected"
        val language = sharedPreferences.getString(SharedPreferencesKeys.Localization, notSelected)
        if (language == notSelected || language == null) {
            return null
        }
        return Localization.values().find { it.name == language }
    }

    private fun updateResources(context: Context, language: Localization): Context? {
        val locale = Locale(language.localeKey)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }
}
