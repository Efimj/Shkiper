package com.jobik.shkiper.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.jobik.shkiper.R

class LauncherIcon {
    @Keep
    enum class LauncherActivity(
        val activity: String,
        @DrawableRes val drawable: Int,
        @StringRes val header: Int
    ) {
        Classic(
            activity = "activity.MainActivity",
            drawable = R.drawable.ic_classic_launcher,
            header = R.string.classic
        ),
        ClassicWhite(
            activity = "ClassicWhiteLauncher",
            drawable = R.drawable.ic_classic_white_launcher, header = R.string.classic_white
        ),
        Material(
            activity = "MaterialLauncher",
            drawable = R.drawable.ic_material_launcher,
            header = R.string.material
        ),

        Love(
            activity = "LoveLauncher",
            drawable = R.drawable.ic_love_launcher,
            header = R.string.love
        ),
        Rocket(
            activity = "RocketLauncher",
            drawable = R.drawable.ic_rocket_launcher,
            header = R.string.rocket
        ),
        Money(
            activity = "MoneyLauncher",
            drawable = R.drawable.ic_money_launcher,
            header = R.string.bank
        ),
        Star(
            activity = "StarLauncher",
            drawable = R.drawable.ic_star_launcher,
            header = R.string.star
        ),
        Night(
            activity = "NightLauncher",
            drawable = R.drawable.ic_night_launcher,
            header = R.string.night
        ),

        Strawberry(
            activity = "StrawberryLauncher",
            drawable = R.drawable.ic_strawberry_launcher,
            header = R.string.strawberry
        ),
        Mango(
            activity = "MangoLauncher",
            drawable = R.drawable.ic_mango_launcher,
            header = R.string.mango
        ),
        Avocado(
            activity = "AvocadoLauncher",
            drawable = R.drawable.ic_avocado_launcher,
            header = R.string.avocado
        ),
        Blueberry(
            activity = "BlueberryLauncher",
            drawable = R.drawable.ic_blueberry_launcher,
            header = R.string.blueberry
        ),
        Plum(
            activity = "PlumLauncher",
            drawable = R.drawable.ic_plum_launcher,
            header = R.string.plum
        );

        fun toActivityAliasName(context: Context): String {
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