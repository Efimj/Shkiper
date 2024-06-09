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
        Classic(
            activity = "activity.MainActivity",
            drawable = R.drawable.ic_classic_launcher_foreground
        ),
        ClassicWhite(
            activity = "ClassicWhiteLauncher",
            drawable = R.drawable.ic_classic_white_launcher_foreground
        ),
        Avocado(activity = "AvocadoLauncher", drawable = R.drawable.ic_avocado_launcher_foreground),
        Blueberry(
            activity = "BlueberryLauncher",
            drawable = R.drawable.ic_blueberry_launcher_foreground
        ),
        Mango(
            activity = "MangoLauncher",
            drawable = R.drawable.ic_mango_launcher_foreground
        ),
        Plum(
            activity = "PlumLauncher",
            drawable = R.drawable.ic_plum_launcher_foreground
        ),
        Strawberry(
            activity = "StrawberryLauncher",
            drawable = R.drawable.ic_strawberry_launcher_foreground
        ),
        Material(
            activity = "MaterialLauncher",
            drawable = R.drawable.ic_material_launcher_foreground
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
}