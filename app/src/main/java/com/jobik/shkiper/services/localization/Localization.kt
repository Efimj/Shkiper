package com.jobik.shkiper.services.localization

import android.content.Context
import com.jobik.shkiper.R

enum class Localization(val localeKey: String) {
    EN("en"),
    UK("uk"),
    RU("ru");

    fun getLocalizedValue(context: Context): String {
        val string: String = when (name) {
            EN.name -> context.getString(R.string.en)
            UK.name -> context.getString(R.string.uk)
            RU.name -> context.getString(R.string.ru)

            else -> ""
        }
        return string
    }
}