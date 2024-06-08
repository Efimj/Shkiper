package com.jobik.shkiper.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.jobik.shkiper.R

class LauncherIcon {
    @Keep
    enum class LauncherActivity(val activity: String, @DrawableRes val drawable: Int) {
        Default(activity = "activity.MainActivity", drawable = R.drawable.ic_app),
        Minimalism(activity = "MaterialActivity", drawable = R.drawable.ic_notification);

        fun toActivityAliasName(context: Context): String {
            println(context.packageName)
            return this.activity.let { context.packageName + ".$it" }
        }
    }

    fun switchLauncherIcon(context: Context, activity: LauncherActivity) {
        val packageManager = context.packageManager
        LauncherActivity.entries.forEach {
            val state = if (it.activity == activity.activity)
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED

            packageManager.setComponentEnabledSetting(
                /* componentName = */ ComponentName(
                    context,
                    it.toActivityAliasName(context = context)
                ),
                /* newState = */ state,
                /* flags = */ PackageManager.DONT_KILL_APP
            )
        }
    }
}