package com.jobik.shkiper.crash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import kotlin.system.exitProcess

class GlobalExceptionHandler<T : CrashHandler> private constructor(
    private val applicationContext: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler?,
    private val activityToBeLaunched: Class<T>
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(
        p0: Thread,
        p1: Throwable
    ) {
        runCatching {
            Log.e(this.toString(), p1.stackTraceToString())
            applicationContext.launchActivity(activityToBeLaunched, p1)
            exitProcess(0)
        }.getOrElse {
            defaultHandler?.uncaughtException(p0, p1)
        }
    }

    private fun <T : Activity> Context.launchActivity(
        activity: Class<T>,
        exception: Throwable
    ) = applicationContext.startActivity(
        Intent(applicationContext, activity).putExtra(
            INTENT_DATA_NAME,
            "${exception::class.java.simpleName}\n\n${Log.getStackTraceString(exception)}"
        ).addFlags(defFlags)
    )

    companion object {
        fun <T : CrashHandler> initialize(
            applicationContext: Context,
            activityToBeLaunched: Class<T>,
        ) = Thread.setDefaultUncaughtExceptionHandler(
            GlobalExceptionHandler(
                applicationContext = applicationContext,
                defaultHandler = Thread.getDefaultUncaughtExceptionHandler()!!,
                activityToBeLaunched = activityToBeLaunched
            )
        )
    }
}

private const val INTENT_DATA_NAME = "GlobalExceptionHandler"
private const val defFlags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
        Intent.FLAG_ACTIVITY_NEW_TASK or
        Intent.FLAG_ACTIVITY_CLEAR_TASK

abstract class CrashHandler : ComponentActivity() {
    fun getCrashReason(): String = intent.getStringExtra(INTENT_DATA_NAME) ?: ""
}