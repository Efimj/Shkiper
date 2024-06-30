package com.jobik.shkiper

import android.app.Application
import android.content.Context
import com.jobik.shkiper.services.billing.BillingService
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.settings.SettingsManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotepadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    val billingClientLifecycle: BillingService
        get() = BillingService.getInstance(this)

    override fun attachBaseContext(base: Context) {
        SettingsManager.init(base)
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = base,
                language = SettingsManager.settings.localization
            )
        )
    }
}
