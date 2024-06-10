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
        Classic(activity = "activity.MainActivity", drawable = R.drawable.ic_classic_launcher),
        ClassicWhite(
            activity = "ClassicWhiteLauncher",
            drawable = R.drawable.ic_classic_white_launcher
        ),
        Material(activity = "MaterialLauncher", drawable = R.drawable.ic_material_launcher),

        Love(activity = "LoveLauncher", drawable = R.drawable.ic_love_launcher),
        Rocket(activity = "RocketLauncher", drawable = R.drawable.ic_rocket_launcher),
        Money(activity = "MoneyLauncher", drawable = R.drawable.ic_money_launcher),
        Star(activity = "StarLauncher", drawable = R.drawable.ic_star_launcher),
        Night(activity = "NightLauncher", drawable = R.drawable.ic_night_launcher),

        Strawberry(activity = "StrawberryLauncher", drawable = R.drawable.ic_strawberry_launcher),
        Mango(activity = "MangoLauncher", drawable = R.drawable.ic_mango_launcher),
        Avocado(activity = "AvocadoLauncher", drawable = R.drawable.ic_avocado_launcher),
        Blueberry(activity = "BlueberryLauncher", drawable = R.drawable.ic_blueberry_launcher),
        Plum(activity = "PlumLauncher", drawable = R.drawable.ic_plum_launcher);

        fun toActivityAliasName(context: Context): String {
            return this.activity.let { context.packageName + ".$it" }
        }

        fun toJavaClass(context: Context): Class<*> {
            return Class.forName(toActivityAliasName(context = context))
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

    fun getEnabledLauncher(context: Context): LauncherActivity? {
        val packageManager = context.packageManager
        LauncherActivity.entries.forEach {
            val enabled = packageManager.getComponentEnabledSetting(
                ComponentName(
                    context,
                    it.toActivityAliasName(context = context)
                ),
            ) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED

            if (enabled) return it
        }
        return null
    }
}