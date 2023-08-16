package com.jobik.shkiper

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.*
import com.jobik.shkiper.util.ThemePreferenceUtil
import com.jobik.shkiper.services.billing_service.BillingService
import com.jobik.shkiper.services.localization.LocaleHelper
import com.jobik.shkiper.services.localization.Localization

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotepadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setAppTheme(applicationContext)
    }

    val billingClientLifecycle: BillingService
        get() = BillingService.getInstance(this)

    private fun setAppTheme(context: Context) {
        setDefaultNightMode(if (ThemePreferenceUtil(context).getSavedUserTheme().isDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
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
