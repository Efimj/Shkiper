package com.jobik.shkiper.services.localization

import android.content.Context
import androidx.annotation.Keep
import com.jobik.shkiper.R

@Keep
enum class Localization(val localeKey: String) {
    DE("de"),
    EN("en"),
    ES("es"),
    FR("fr"),
    IN("in"),
    IT("it"),
    JA("ja"),
    PT("pt"),
    RU("ru"),
    TH("th"),
    UK("uk"),
    ZH("zh");


    fun getLocalizedValue(context: Context): String {
        val string: String = when (name) {
            EN.name -> context.getString(R.string.en)
            DE.name -> context.getString(R.string.de)
            ES.name -> context.getString(R.string.es)
            FR.name -> context.getString(R.string.fr)
            IN.name -> context.getString(R.string.`in`)
            IT.name -> context.getString(R.string.it)
            JA.name -> context.getString(R.string.ja)
            PT.name -> context.getString(R.string.pt)
            RU.name -> context.getString(R.string.ru)
            TH.name -> context.getString(R.string.th)
            UK.name -> context.getString(R.string.uk)
            ZH.name -> context.getString(R.string.zh)

            else -> ""
        }
        return string
    }
}