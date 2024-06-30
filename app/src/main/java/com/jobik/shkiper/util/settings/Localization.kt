package com.jobik.shkiper.util.settings

import android.content.Context
import androidx.annotation.Keep
import com.jobik.shkiper.R

data class LocaleData(
    val name: String,
    val language: String
)

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
    TR("tr"),
    UK("uk"),
    VI("vi"),
    ZH("zh");

    fun getLocalizedValue(context: Context): LocaleData {
        val string: LocaleData = when (name) {
            EN.name -> LocaleData(
                name = context.getString(R.string.en),
                language = context.getString(R.string.en_language)
            )

            DE.name -> LocaleData(
                name = context.getString(R.string.de),
                language = context.getString(R.string.de_language)
            )

            ES.name -> LocaleData(
                name = context.getString(R.string.es),
                language = context.getString(R.string.es_language)
            )

            FR.name -> LocaleData(
                name = context.getString(R.string.fr),
                language = context.getString(R.string.fr_language)
            )

            IN.name -> LocaleData(
                name = context.getString(R.string.`in`),
                language = context.getString(R.string.in_language)
            )

            IT.name -> LocaleData(
                name = context.getString(R.string.it),
                language = context.getString(R.string.it_language)
            )

            JA.name -> LocaleData(
                name = context.getString(R.string.ja),
                language = context.getString(R.string.ja_language)
            )

            PT.name -> LocaleData(
                name = context.getString(R.string.pt),
                language = context.getString(R.string.pt_language)
            )

            RU.name -> LocaleData(
                name = context.getString(R.string.ru),
                language = context.getString(R.string.ru_language)
            )

            TH.name -> LocaleData(
                name = context.getString(R.string.th),
                language = context.getString(R.string.th_language)
            )

            TR.name -> LocaleData(
                name = context.getString(R.string.tr),
                language = context.getString(R.string.tr_language)
            )

            UK.name -> LocaleData(
                name = context.getString(R.string.uk),
                language = context.getString(R.string.uk_language)
            )

            VI.name -> LocaleData(
                name = context.getString(R.string.vi),
                language = context.getString(R.string.vi_language)
            )

            ZH.name -> LocaleData(
                name = context.getString(R.string.zh),
                language = context.getString(R.string.zh_language)
            )

            else -> LocaleData(name = "NOT SUPPORTED", language = "NOT SUPPORTED")
        }
        return string
    }
}