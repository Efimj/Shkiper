package com.jobik.shkiper.services.backup_service

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.gun0912.tedpermission.coroutine.TedPermission
import java.io.*
import java.time.LocalDate

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
    companion object {
        fun getFileName(): String {
            return "Shkiper backup ${LocalDate.now()}.json"
        }
    }

    private val gson = Gson()

    suspend fun createBackup(backupData: BackupData, context: Context): BackupServiceResult {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // version >= 29 (Android 10, 11, ...)
                if (writeFileNewApi(context, backupData)) return BackupServiceResult.Complete
                return BackupServiceResult.UnexpectedError
            } else {
                // version < 29 (Android ..., 7,8,9)
                if (!checkWritePermission()) return BackupServiceResult.WritePermissionDenied

                if (writeFileOldApi(backupData)) return BackupServiceResult.Complete
                return BackupServiceResult.UnexpectedError
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
            if (jsonText.isEmpty()) UploadResult(BackupServiceResult.UnexpectedError, null) else return UploadResult(
                BackupServiceResult.Complete,
                gson.fromJson(jsonText, BackupData::class.java)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return UploadResult(BackupServiceResult.UnexpectedError, null)
        }
    }

    private fun writeFileOldApi(backupData: BackupData): Boolean {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(downloadsDir, getFileName())

        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(gson.toJson(backupData).toByteArray())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileOutputStream?.close()
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun writeFileNewApi(
        context: Context,
        backupData: BackupData
    ): Boolean {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, getFileName())
            put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        var outputStream: OutputStream? = null
        try {
            val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val uri = contentResolver.insert(contentUri, contentValues)
            outputStream = uri?.let { contentResolver.openOutputStream(it) }
            outputStream?.write(gson.toJson(backupData).toByteArray())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
        return false
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