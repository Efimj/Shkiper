package com.android.notepad.screens.SettingsScreen

data class SettingsScreenState (
    val isLocalBackupUploading:Boolean = false,
    val isLocalBackupSaving:Boolean = false,
    val isGoogleDriveBackupUploading:Boolean = false,
    val isGoogleDriveBackupUpSaving:Boolean = false,
)