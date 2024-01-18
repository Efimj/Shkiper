package com.jobik.shkiper.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream

class FileHelper {
    companion object {
        fun saveImageToInternalStorage(
            context: Context,
            bitmap: ImageBitmap,
            fileName: String,
            compressFormat: CompressFormat = CompressFormat.PNG
        ) {
            val outputStream: FileOutputStream
            try {
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                bitmap.asAndroidBitmap().compress(compressFormat, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun saveFileToInternalStorage(context: Context, fileUri: Uri, fileName: String) {
            try {
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}