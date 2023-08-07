package com.jobik.shkiper.services.in_app_updates_service

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.material3.SnackbarDuration
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.SharedPreferencesKeys.LastUpdateCanceledRequestDate
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class InAppUpdatesService(val activity: Activity) {
    private val sharedPreferences =
        activity.applicationContext.getSharedPreferences(
            SharedPreferencesKeys.ApplicationStorageName,
            Context.MODE_PRIVATE
        )
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE
    private val minimumTimeBetweenCancelUpdates = 5L

    // Create a listener to track request state updates.
    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            snackbarShowUpdate()
        }
    }

    fun checkForUpdate(updateActivityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                snackbarShowUpdate()
            } else {
                updateLastCanceledUpdateDate()
                if (!checkUpdateNeed()) return@addOnSuccessListener
                val isUpdateAvailable = updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                val isUpdateAllowed = when (updateType) {
                    AppUpdateType.FLEXIBLE -> updateInfo.isFlexibleUpdateAllowed
                    AppUpdateType.IMMEDIATE -> updateInfo.isImmediateUpdateAllowed
                    else -> false
                }
                if (isUpdateAvailable && isUpdateAllowed) {
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        updateActivityResultLauncher,
                        AppUpdateOptions.newBuilder(updateType)
                            .setAllowAssetPackDeletion(true)
                            .build()
                    )
                }
            }
            registerListener()
        }
    }

    private fun registerListener() {
        if (updateType == AppUpdateType.FLEXIBLE)
            appUpdateManager.registerListener(listener)
    }

    private fun unregisterListener() {
        // When status updates are no longer needed, unregister the listener.
        if (updateType == AppUpdateType.FLEXIBLE)
            appUpdateManager.unregisterListener(listener)
    }

    private fun checkUpdateNeed(): Boolean {
        return try {
            val lastUpdateDate =
                LocalDateTime.parse(
                    sharedPreferences.getString(
                        LastUpdateCanceledRequestDate,
                        LocalDateTime.now().minusDays(minimumTimeBetweenCancelUpdates + 1).toString()
                    )
                )
            val daysDifference = LocalDateTime.now().minusDays(minimumTimeBetweenCancelUpdates)
            lastUpdateDate.isBefore(daysDifference)
        } catch (e: Exception) {
            true
        }
    }

    fun updateLastCanceledUpdateDate() {
        val editor = sharedPreferences.edit()
        editor.putString(LastUpdateCanceledRequestDate, LocalDateTime.now().toString())
        editor.apply()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun snackbarShowUpdate() {
        GlobalScope.launch() {
            SnackbarHostUtil.snackbarHostState.showSnackbar(
                SnackbarVisualsCustom(
                    message = activity.applicationContext.getString(R.string.UpdateDownloaded),
                    actionLabel = activity.applicationContext.getString(R.string.Restart),
                    duration = SnackbarDuration.Indefinite,
                    action = { appUpdateManager.completeUpdate(); unregisterListener() }
                )
            )
        }
    }
}