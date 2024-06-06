package com.jobik.shkiper.services.backup

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import com.google.gson.Gson
import com.gun0912.tedpermission.coroutine.TedPermission
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Keep
enum class BackupServiceResult {
    Complete,
    WritePermissionDenied,
    ReadPermissionDenied,
    UnexpectedError
}

@Keep
data class UploadResult(
    val backupServiceResult: BackupServiceResult,
    val backupData: BackupData?
)

@Keep
class BackupService {
    val LogCatTag = "BackupService"

    companion object {
        fun getFileName(): String {
            return "shkiper ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}.json"
        }

        const val BackupType = "application/json"
    }

    private val gson = Gson()

    suspend fun createBackup(
        uri: Uri,
        backupData: BackupData,
        context: Context
    ): BackupServiceResult {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // version >= 29 (Android 10, 11, ...)
            } else {
                // version < 29 (Android ..., 7,8,9)
                if (!checkWritePermission()) {
                    return BackupServiceResult.WritePermissionDenied
                }
            }

            val result = writeFile(uri = uri, context = context, backupData = backupData)

            if (result) {
                return BackupServiceResult.Complete
            }
        }
        return BackupServiceResult.UnexpectedError
    }

    suspend fun uploadBackup(uri: Uri, context: Context): UploadResult {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // version >= 29 (Android 10, 11, ...)

        } else {
            // version < 29 (Android ..., 7,8,9)
            if (!checkReadPermission())
                return UploadResult(BackupServiceResult.ReadPermissionDenied, null)
        }

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }
            if (jsonText.isEmpty()) UploadResult(
                BackupServiceResult.UnexpectedError,
                null
            ) else return UploadResult(
                BackupServiceResult.Complete,
                gson.fromJson(jsonText, BackupData::class.java)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w(LogCatTag, e)
            return UploadResult(BackupServiceResult.UnexpectedError, null)
        }
    }

    private fun writeFile(
        uri: Uri,
        context: Context,
        backupData: BackupData
    ): Boolean {
        try {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
            outputStream?.use {
                it.write(gson.toJson(backupData).toByteArray())
            }
            outputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w(LogCatTag, e)
            return false
        }
        return true
    }

    private suspend fun checkWritePermission(): Boolean {
        return TedPermission.create()
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .checkGranted()
    }

    private suspend fun checkReadPermission(): Boolean {
        return TedPermission.create()
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .checkGranted()
    }
}