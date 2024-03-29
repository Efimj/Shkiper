package com.jobik.shkiper.screens.SettingsScreen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.R
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.services.backup_service.BackupData
import com.jobik.shkiper.services.backup_service.BackupService
import com.jobik.shkiper.services.backup_service.BackupServiceResult
import com.jobik.shkiper.services.localization.LocaleHelper
import com.jobik.shkiper.services.localization.Localization
import com.jobik.shkiper.services.review_service.ReviewService
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.ThemeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsScreenState(
    val isLocalBackupUploading: Boolean = false,
    val isLocalBackupSaving: Boolean = false,
    val isGoogleDriveBackupUploading: Boolean = false,
    val isGoogleDriveBackupUpSaving: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    private val application: Application,
) : ViewModel() {

    private val _settingsScreenState = mutableStateOf(SettingsScreenState())
    val settingsScreenState: State<SettingsScreenState> = _settingsScreenState

    /*******************
     * App theming
     *******************/

    fun toggleAppTheme() {
        ThemeUtil.saveThemeMode(
            context = application.applicationContext,
            mode = !(ThemeUtil.isDarkMode.value ?: false),
            newThemeStyle = ThemeUtil.themeStyle.value ?: CustomThemeStyle.PastelPurple
        )
    }

    fun selectColorTheme(theme: CustomThemeStyle) {
        ThemeUtil.saveThemeMode(
            context = application.applicationContext,
            mode = ThemeUtil.isDarkMode.value ?: false,
            newThemeStyle = theme
        )
    }

    /*******************
     * App languages
     *******************/

    fun getLocalizationList(localContext: Context): List<String> {
        return Localization.values().filter { it.name != NotepadApplication.currentLanguage.name }
            .map { it.getLocalizedValue(localContext) }
    }

    fun selectLocalization(selectedIndex: Int) {
        try {
            val newLocalization: Localization =
                Localization.values().filter { it.name != NotepadApplication.currentLanguage.name }[selectedIndex]
            LocaleHelper.setLocale(application.applicationContext, newLocalization)
        } catch (e: Exception) {
            Log.d("ChangeLocalizationError", e.message.toString())
        }
    }

    /*******************
     * App backups
     *******************/

    fun saveLocalBackup() {
        if (isBackupHandling()) return
        _settingsScreenState.value = _settingsScreenState.value.copy(isLocalBackupSaving = true)
        viewModelScope.launch() {
            val backupData = BackupData()
            backupData.realmNoteList = noteRepository.getAllNotes()
            backupData.realmReminderList = reminderRepository.getAllReminders()
            backupData.userStatistics = StatisticsService(application.applicationContext).appStatistics.statisticsData
            val result = BackupService().createBackup(backupData, application.applicationContext)
            delay(1000)
            _settingsScreenState.value = _settingsScreenState.value.copy(isLocalBackupSaving = false)
            when (result) {
                BackupServiceResult.UnexpectedError -> showSnackbar(
                    application.applicationContext.getString(R.string.UnspecifiedErrorOccurred),
                    Icons.Default.Error
                )

                BackupServiceResult.WritePermissionDenied -> showSnackbar(
                    application.applicationContext.getString(R.string.CannotPerformedWithoutPermission),
                    Icons.Default.Settings
                )

                else -> showSnackbar(
                    application.applicationContext.getString(R.string.BackupCreated),
                    Icons.Default.Done
                )
            }
        }
    }

    fun uploadLocalBackup(uri: Uri) {
        if (isBackupHandling()) return
        _settingsScreenState.value = _settingsScreenState.value.copy(isLocalBackupUploading = true)
        viewModelScope.launch() {
            val result = BackupService().uploadBackup(uri, application.applicationContext)
            if (result.backupData != null) {
                noteRepository.insertOrUpdateNotes(result.backupData.realmNoteList, false)
                reminderRepository.insertOrUpdateReminders(result.backupData.realmReminderList, false)
                StatisticsService(application.applicationContext).updateStatistics(result.backupData.userStatistics)
            }
            delay(1000)
            _settingsScreenState.value = _settingsScreenState.value.copy(isLocalBackupUploading = false)
            when (result.backupServiceResult) {
                BackupServiceResult.UnexpectedError -> showSnackbar(
                    application.applicationContext.getString(R.string.UnspecifiedErrorOccurred),
                    Icons.Default.Error
                )

                BackupServiceResult.ReadPermissionDenied -> showSnackbar(
                    application.applicationContext.getString(R.string.CannotPerformedWithoutPermission),
                    Icons.Default.Settings
                )

                else -> showSnackbar(
                    application.applicationContext.getString(R.string.BackupUploaded),
                    Icons.Default.Done
                )
            }
        }
    }

    private suspend fun showSnackbar(message: String, icon: ImageVector?) {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = message,
                icon = icon
            )
        )
    }

    fun isBackupHandling(): Boolean {
        return _settingsScreenState.value.isLocalBackupUploading ||
                _settingsScreenState.value.isGoogleDriveBackupUploading ||
                _settingsScreenState.value.isGoogleDriveBackupUpSaving ||
                _settingsScreenState.value.isLocalBackupSaving
    }

    /*******************
     * App Rate
     *******************/

    fun rateTheApp() {
        ReviewService(application.applicationContext).openRateScreen()
    }
}