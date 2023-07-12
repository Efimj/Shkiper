package com.android.notepad.helpers.localization

import android.content.Context
import com.android.notepad.R

enum class Localization(val localeKey: String) {
    EN("en"),
    UK("uk"),
    RU("ru");

    fun getLocalizedValue(context: Context): String {
        val string: String = when (name) {
            Localization.EN.name -> context.getString(R.string.en)
            Localization.UK.name -> context.getString(R.string.uk)
            Localization.RU.name -> context.getString(R.string.ru)

            else -> ""
        }
        return string
    }
}