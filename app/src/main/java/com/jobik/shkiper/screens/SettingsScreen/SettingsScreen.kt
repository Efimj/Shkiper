package com.jobik.shkiper.screens.SettingsScreen

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.R
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.DropDownButton
import com.jobik.shkiper.ui.components.buttons.DropDownButtonSizeMode
import com.jobik.shkiper.ui.components.buttons.DropDownItem
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.components.cards.ThemePreview
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.util.ThemeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
                color = CustomTheme.colors.text,
                text = stringResource(R.string.Application),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(
                stringResource(R.string.ApplicationTheme),
                if (ThemeUtil.isDarkMode.value
                        ?: isSystemInDarkTheme()
                ) stringResource(R.string.LightTheme) else stringResource(R.string.DarkTheme),
                buttonIcon = if (ThemeUtil.isDarkMode.value
                        ?: isSystemInDarkTheme()
                ) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
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
                color = CustomTheme.colors.text,
                text = stringResource(R.string.Backup),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Spacer(Modifier.height(8.dp))
            SettingsItem(
                description = stringResource(R.string.SaveAllNotesLocally),
                buttonText = if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving)
                    stringResource(R.string.Saving) else
                    stringResource(R.string.Save),
                buttonIcon = if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving) Icons.Outlined.Loop else Icons.Outlined.Download,
                isLoading = settingsViewModel.settingsScreenState.value.isLocalBackupSaving,
                isEnabled = !settingsViewModel.isBackupHandling(),
                onClick = { settingsViewModel.saveLocalBackup() }
            )
            SettingsItem(
                description = stringResource(R.string.UploadNotes),
                buttonText = if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading)
                    stringResource(R.string.Loading) else
                    stringResource(R.string.Upload),
                buttonIcon = if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading) Icons.Outlined.Loop else Icons.Outlined.Upload,
                isLoading = settingsViewModel.settingsScreenState.value.isLocalBackupUploading,
                isEnabled = !settingsViewModel.isBackupHandling(),
                onClick = { fileSearch.launch(arrayOf("*/*")) })
        }
        SettingsItemGroup {
            Text(
                color = CustomTheme.colors.text,
                text = stringResource(R.string.Other),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Spacer(Modifier.height(8.dp))
            SettingsItem(
                stringResource(R.string.AboutNotepad),
                stringResource(R.string.Show),
                Icons.Outlined.Info
            ) { navController.navigate(AppScreens.AboutNotepad.route) }
            SettingsItem(
                stringResource(R.string.StatisticsPage),
                stringResource(R.string.Show),
                Icons.Outlined.DataUsage
            ) { navController.navigate(AppScreens.Statistics.route) }
            SettingsItem(
                stringResource(R.string.OnboardingPage),
                stringResource(R.string.Open),
                Icons.Outlined.ViewCarousel
            ) { navController.navigate(AppScreens.Onboarding.route) }
        }
        SettingsItemGroup {
            Text(
                color = CustomTheme.colors.text,
                text = stringResource(R.string.Support),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
            )
            Spacer(Modifier.height(8.dp))
            SettingsItem(
                stringResource(R.string.RateTheApp),
                stringResource(R.string.Open),
                Icons.Outlined.Stars
            ) { settingsViewModel.rateTheApp() }
            SettingsItem(
                stringResource(R.string.SupportDevelopment),
                stringResource(R.string.Donate),
                Icons.Outlined.LocalMall
            ) { navController.navigate(AppScreens.Purchases.route) }
        }
        SettingsItemGroup {
            Text(
                color = CustomTheme.colors.text,
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
                    tint = CustomTheme.colors.textSecondary
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.AppDataPolitics),
                    color = CustomTheme.colors.textSecondary,
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
//    val isDark = ThemeUtil.theme.isDarkTheme
//    val selectedThemeName = if (isDark) ThemeUtil.theme.darkThemeName else ThemeUtil.theme.lightThemeName

    val colorValues =
        if (ThemeUtil.isDarkMode.value != false) CustomThemeStyle.entries.map { it.dark } else CustomThemeStyle.entries.map { it.light }
    val colorValuesName = CustomThemeStyle.entries
    val selectedThemeName = ThemeUtil.themeStyle.value?.name ?: CustomThemeStyle.DarkPurple.name

    Column(
        Modifier.padding(vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    color = CustomTheme.colors.text,
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
            items(colorValues.size) { theme ->
                Box(Modifier.padding(end = 10.dp).height(70.dp).width(55.dp)) {
                    ThemePreview(
                        colors = colorValues[theme],
                        selected = colorValuesName[theme].name == selectedThemeName
                    ) {
                        settingsViewModel.selectColorTheme(theme = colorValuesName[theme])
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItemGroup(columnScope: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp).widthIn(max = 500.dp),
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
    val dropDownItems = remember { settingsViewModel.getLocalizationList(context).map { DropDownItem(text = it) } }

    DropDownButton(
        dropDownItems,
        currentLanguage.ordinal,
        Modifier.width(150.dp),
        DropDownButtonSizeMode.STRERCHBYCONTENT,
        onChangedSelection = {
            settingsViewModel.selectLocalization(it)
            recreateActivity(context, coroutineScope)
        }) {
        CustomButton(
            modifier = Modifier.width(145.dp),
            text = currentLanguage.getLocalizedValue(context),
            icon = Icons.Outlined.Language,
            onClick = { it() },
            border = BorderStroke(1.dp, CustomTheme.colors.stroke),
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
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    onClick: (() -> Unit) = { },
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                color = CustomTheme.colors.text,
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
                CustomButton(
                    text = buttonText,
                    icon = buttonIcon,
                    modifier = Modifier.width(145.dp),
                    onClick = onClick,
                    enabled = !isLoading && isEnabled,
                    loading = isLoading,
                    colors = if (isLoading) ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.active,
                        disabledBackgroundColor = CustomTheme.colors.active
                    ) else ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.mainBackground,
                        disabledBackgroundColor = Color.Transparent
                    ),
                    textColor = if (isLoading) Color.White else CustomTheme.colors.text,
                    iconTint = if (isLoading) Color.White else CustomTheme.colors.text,
                )
            else {
                buttonComponent()
            }
        }
    }
}
