package com.android.notepad.services.backup_service

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toFile
import com.android.notepad.SharedPreferencesKeys
import com.android.notepad.services.statistics_service.StatisticsData
import com.google.gson.Gson
import java.io.*

class BackupService {
    companion object {
        const val fileName: String = "Shkiper backup.json"
    }

    private val gson = Gson()

    fun createBackup(backupData: BackupData): Boolean {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        return try {
            File(downloadsDirectory, fileName).writeText(gson.toJson(backupData))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun uploadBackup(uri: Uri, context:Context): BackupData? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }
            if (jsonText.isEmpty()) null else gson.fromJson(jsonText, BackupData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}