package com.android.notepad.screens.SettingsScreen

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.notepad.NotepadApplication
import com.android.notepad.R
import com.android.notepad.navigation.AppScreens
import com.android.notepad.services.backup_service.BackupService
import com.android.notepad.ui.components.buttons.DropDownButton
import com.android.notepad.ui.components.buttons.DropDownButtonSizeMode
import com.android.notepad.ui.components.buttons.DropDownItem
import com.android.notepad.ui.components.buttons.RoundedButton
import com.android.notepad.ui.components.cards.ThemePreview
import com.android.notepad.ui.theme.CustomAppTheme
import com.android.notepad.util.ThemeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val fileSearch =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                settingsViewModel.uploadLocalBackup(uri)
            }
        }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(75.dp))
        SettingsItemGroup {
            Text(
                color = CustomAppTheme.colors.text,
                text = stringResource(R.string.Application),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(
                stringResource(R.string.ApplicationTheme),
                ThemeUtil.theme.let { if (it.isDarkTheme) stringResource(R.string.LightTheme) else stringResource(R.string.DarkTheme) },
                buttonIcon = if (ThemeUtil.theme.isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                onClick = { settingsViewModel.toggleAppTheme() }
            )
            SettingsColorThemePicker(settingsViewModel)
            SettingsItem(
                stringResource(R.string.ChoseLocalization), null, null,
                {
                    SettingsItemSelectLanguage(settingsViewModel)
                })
        }
        SettingsItemGroup {
            Text(
                color = CustomAppTheme.colors.text,
                text = stringResource(R.string.Backup),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Spacer(Modifier.height(8.dp))
            SettingsItem(
                description = stringResource(R.string.SaveAllNotesLocally),
                buttonText = stringResource(R.string.Save),
                buttonIcon = Icons.Outlined.Download,
                onClick = { settingsViewModel.saveLocalBackup() }
            )
            SettingsItem(
                stringResource(R.string.UploadNotes),
                stringResource(R.string.Upload),
                Icons.Outlined.Upload,
                onClick = { fileSearch.launch(arrayOf("*/*")) })
            SettingsItem(
                stringResource(R.string.CloudStorage),
                stringResource(R.string.Connect),
                Icons.Outlined.CloudOff,
                onClick = {})
        }
        SettingsItemGroup {
            Text(
                color = CustomAppTheme.colors.text,
                text = stringResource(R.string.Other),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Spacer(Modifier.height(8.dp))
            SettingsItem(
                stringResource(R.string.OnboardingPage),
                stringResource(R.string.Open),
                Icons.Outlined.ViewCarousel
            ) { navController.navigate(AppScreens.Onboarding.route) }
            SettingsItem(
                stringResource(R.string.StatisticsPage),
                stringResource(R.string.Open),
                Icons.Outlined.DataUsage
            ) { navController.navigate(AppScreens.Statistics.route) }
        }
        SettingsItemGroup {
            Text(
                color = CustomAppTheme.colors.text,
                text = stringResource(R.string.Information),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Row(
                Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.Info),
                    tint = CustomAppTheme.colors.textSecondary
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.AppDataPolitics),
                    color = CustomAppTheme.colors.textSecondary,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(55.dp))
    }
}

@Composable
private fun SettingsColorThemePicker(settingsViewModel: SettingsViewModel) {
    val isDark = ThemeUtil.theme.isDarkTheme
    val selectedThemeName = if (isDark) ThemeUtil.theme.darkThemeName else ThemeUtil.theme.lightThemeName

    Column(
        Modifier.padding(vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    color = CustomAppTheme.colors.text,
                    text = stringResource(R.string.ApplicationColors),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(6.dp))
        LazyRow(state = rememberLazyListState(), contentPadding = PaddingValues(start = 10.dp)) {
            items(settingsViewModel.themeColorList.value) { theme ->
                Box(Modifier.padding(end = 10.dp).height(70.dp).width(55.dp)) {
                    val colors = if (isDark) theme.colorTheme.darkColors else theme.colorTheme.lightColors
                    ThemePreview(colors, theme.name == selectedThemeName) {
                        settingsViewModel.selectColorTheme(theme.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItemGroup(columnScope: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(10.dp))
        columnScope()
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun SettingsItemSelectLanguage(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentLanguage = NotepadApplication.currentLanguage

    val dropDownItems = settingsViewModel.localizationList.value.map { DropDownItem(text = it) }

    DropDownButton(dropDownItems,
        currentLanguage.ordinal,
        Modifier.width(150.dp),
        DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH,
        onChangedSelection = {
            settingsViewModel.selectLocalization(it)
            recreateActivity(context, coroutineScope)
        }) {
        RoundedButton(
            modifier = Modifier.width(145.dp),
            text = currentLanguage.getLocalizedValue(context),
            icon = Icons.Outlined.Language,
            onClick = { it() },
            border = BorderStroke(1.dp, CustomAppTheme.colors.stroke),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent
            )
        )
    }
}

private fun recreateActivity(context: Context, scope: CoroutineScope) {
    /*******************
     * If you remove the delay, an error will be generated
     * ANR in com.example.notepadapp (com.example.notepadapp/.activity.MainActivity)
     * PID: 16898
     * Reason: Input dispatching timed out (Waiting because no window has focus but there is a focused application that may eventually add a window when it finishes starting up.)
     * Load: 0.85 / 0.24 / 0.12
     * possible problem drop down layout
     *******************/

    scope.launch {
        delay(150)
        (context as? Activity)?.recreate()
    }
}

@Composable
private fun SettingsItem(
    description: String,
    buttonText: String? = null,
    buttonIcon: ImageVector? = null,
    buttonComponent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit) = { },
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                color = CustomAppTheme.colors.text,
                text = description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterEnd),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (buttonComponent == null)

                RoundedButton(
                    text = buttonText,
                    icon = buttonIcon,
                    modifier = Modifier.width(145.dp),
                    onClick = onClick,
                )
            else {
                buttonComponent()
            }
        }
    }
}
