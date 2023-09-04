package com.jobik.shkiper.widgets

import androidx.annotation.Keep
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.action.ActionParameters

@Keep
object WidgetKeys {
    const val NOTE_ID = "NOTE_ID"
    object Prefs {
        val noteId = stringPreferencesKey("noteId")
        val noteHeader = stringPreferencesKey("noteHeader")
        val noteBody = stringPreferencesKey("noteBody")
        val noteLastUpdate = stringPreferencesKey("noteLastUpdate")
    }

    object Params {
        val noteIdParam = ActionParameters.Key<String>(NOTE_ID)
    }
}