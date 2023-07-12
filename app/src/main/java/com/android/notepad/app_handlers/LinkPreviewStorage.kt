package com.android.notepad.app_handlers

import java.io.File

class LinkPreviewStorage {
    private val previewPhotosDir: File = File("PreviewPhotos")

    init {
        if (!previewPhotosDir.exists()) {
            previewPhotosDir.mkdir()
        }
    }

    fun addPhoto(photoName: String, photoData: ByteArray) {
        val photoFile = File(previewPhotosDir, photoName)
        photoFile.writeBytes(photoData)
    }

    fun deletePhoto(photoName: String): Boolean {
        val photoFile = File(previewPhotosDir, photoName)
        return if (photoFile.exists()) {
            photoFile.delete()
        } else {
            false
        }
    }

    fun getPhoto(photoName: String): ByteArray? {
        val photoFile = File(previewPhotosDir, photoName)
        return if (photoFile.exists()) {
            photoFile.readBytes()
        } else {
            null
        }
    }

    fun updatePhoto(photoName: String, newPhotoData: ByteArray): Boolean {
        val photoFile = File(previewPhotosDir, photoName)
        return if (photoFile.exists()) {
            photoFile.writeBytes(newPhotoData)
            true
        } else {
            false
        }
    }
}