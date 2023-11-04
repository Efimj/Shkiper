package com.jobik.shkiper.screens.SettingsScreen

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.jobik.shkiper.ui.components.buttons.*
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.ThemePreview
import com.jobik.shkiper.ui.modifiers.circularRotation
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.util.ThemeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(75.dp))
        ProgramSettings(settingsViewModel)
        BackupSettings(settingsViewModel)
        OtherSettings(navController)
        DevSupportSettings(settingsViewModel, navController)
        InformationSettings()
        Spacer(Modifier.height(55.dp))
    }
}

@Composable
private fun InformationSettings() {
    SettingsItemGroup {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            fontSize = 16.sp,
            color = CustomTheme.colors.active,
            text = stringResource(R.string.Information),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.body2,
        )
        Row(
            Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 8.dp).padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.Info),
                tint = CustomTheme.colors.textSecondary
            )
            Spacer(Modifier.width(20.dp))
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
}

@Composable
private fun DevSupportSettings(
    settingsViewModel: SettingsViewModel,
    navController: NavController
) {
    SettingsItemGroup(setAccent = true) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            fontSize = 16.sp,
            color = CustomTheme.colors.active,
            text = stringResource(R.string.Support),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.body2,
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.Stars,
            title = stringResource(R.string.RateTheApp),
            onClick = { settingsViewModel.rateTheApp() }
        )
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.LocalMall,
            title = stringResource(R.string.SupportDevelopment),
            onClick = { navController.navigate(AppScreens.Purchases.route) }
        )
    }
}

@Composable
private fun OtherSettings(navController: NavController) {
    SettingsItemGroup {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            fontSize = 16.sp,
            color = CustomTheme.colors.active,
            text = stringResource(R.string.Other),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.body2,
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.Info,
            title = stringResource(R.string.AboutNotepad),
            onClick = { navController.navigate(AppScreens.AboutNotepad.route) }
        )
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.DataUsage,
            title = stringResource(R.string.StatisticsPage),
            onClick = { navController.navigate(AppScreens.Statistics.route) }
        )
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.ViewCarousel,
            title = stringResource(R.string.OnboardingPage),
            onClick = { navController.navigate(AppScreens.Onboarding.route) }
        )
    }
}

@Composable
private fun BackupSettings(
    settingsViewModel: SettingsViewModel,
) {
    val fileSearch =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                settingsViewModel.uploadLocalBackup(uri)
            }
        }

    SettingsItemGroup {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            fontSize = 16.sp,
            color = CustomTheme.colors.active,
            text = stringResource(R.string.Backup),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.body2,
        )
        Spacer(Modifier.height(8.dp))

        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.Download,
            isEnabled = !settingsViewModel.isBackupHandling(),
            isActive = settingsViewModel.settingsScreenState.value.isLocalBackupSaving,
            title = if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving)
                stringResource(R.string.Saving) else
                stringResource(R.string.Save),
            onClick = { settingsViewModel.saveLocalBackup() }
        ) {
            val iconColor =
                if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving) CustomTheme.colors.textOnActive else CustomTheme.colors.text
            val contentColor = remember { Animatable(iconColor) }

            LaunchedEffect(settingsViewModel.settingsScreenState.value.isLocalBackupSaving) {
                contentColor.animateTo(iconColor, animationSpec = tween(200))
            }

            AnimatedVisibility(
                visible = settingsViewModel.settingsScreenState.value.isLocalBackupSaving,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Icon(
                    imageVector = if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving) Icons.Outlined.Loop else Icons.Outlined.Done,
                    contentDescription = null,
                    tint = contentColor.value,
                    modifier = if (settingsViewModel.settingsScreenState.value.isLocalBackupSaving) Modifier.size(24.dp)
                        .circularRotation() else Modifier.size(24.dp)
                )
            }
        }
        SettingsItem(
            modifier = Modifier.heightIn(min = 50.dp),
            icon = Icons.Rounded.Upload,
            isEnabled = !settingsViewModel.isBackupHandling(),
            isActive = settingsViewModel.settingsScreenState.value.isLocalBackupUploading,
            title = if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading)
                stringResource(R.string.Loading) else
                stringResource(R.string.Upload),
            onClick = { fileSearch.launch(arrayOf("*/*")) }
        ) {
            val iconColor =
                if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading) CustomTheme.colors.textOnActive else CustomTheme.colors.text
            val contentColor = remember { Animatable(iconColor) }

            LaunchedEffect(settingsViewModel.settingsScreenState.value.isLocalBackupUploading) {
                contentColor.animateTo(iconColor, animationSpec = tween(200))
            }

            AnimatedVisibility(
                visible = settingsViewModel.settingsScreenState.value.isLocalBackupUploading,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Icon(
                    imageVector = if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading) Icons.Outlined.Loop else Icons.Outlined.Done,
                    contentDescription = null,
                    tint = contentColor.value,
                    modifier = if (settingsViewModel.settingsScreenState.value.isLocalBackupUploading) Modifier.size(
                        24.dp
                    )
                        .circularRotation() else Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun ProgramSettings(settingsViewModel: SettingsViewModel) {
    SettingsItemGroup {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            fontSize = 16.sp,
            color = CustomTheme.colors.active,
            text = stringResource(R.string.Application),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.body2,
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            icon = Icons.Rounded.Contrast,
            title = stringResource(R.string.ApplicationTheme),
            onClick = { settingsViewModel.toggleAppTheme() }
        ) {
            CustomSwitch(
                active = ThemeUtil.isDarkMode.value ?: false,
                onClick = { settingsViewModel.toggleAppTheme() },
                thumbContent = if (ThemeUtil.isDarkMode.value == true) {
                    {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Outlined.LightMode,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                })
        }
        SettingsColorThemePicker(settingsViewModel)
        Spacer(Modifier.height(4.dp))
        SettingsItemSelectLanguage(settingsViewModel = settingsViewModel)
    }
}

@Composable
private fun SettingsColorThemePicker(settingsViewModel: SettingsViewModel) {
    val colorValues =
        if (ThemeUtil.isDarkMode.value != false) CustomThemeStyle.entries.map { it.dark } else CustomThemeStyle.entries.map { it.light }
    val colorValuesName = CustomThemeStyle.entries
    val selectedThemeName = ThemeUtil.themeStyle.value?.name ?: CustomThemeStyle.PastelPurple.name

    Column(
        Modifier.padding(vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(start = 65.dp),
            color = CustomTheme.colors.text,
            text = stringResource(R.string.ApplicationColors),
            fontSize = 18.sp,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(6.dp))
        LazyRow(state = rememberLazyListState(), contentPadding = PaddingValues(start = 20.dp)) {
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
private fun SettingsItemGroup(setAccent: Boolean = false, columnScope: @Composable ColumnScope.() -> Unit) {
    Spacer(Modifier.height(7.dp))
    Column(
        modifier = Modifier.widthIn(max = 500.dp)
            .padding(horizontal = 10.dp)
            .clip(CustomTheme.shapes.large)
            .background(CustomTheme.colors.secondaryBackground)
            .border(
                width = if (setAccent) 2.dp else 0.dp,
                shape = CustomTheme.shapes.large,
                color = if (setAccent) CustomTheme.colors.active else Color.Transparent
            )
            .padding(top = 13.dp, bottom = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        columnScope()
    }
    Spacer(Modifier.height(7.dp))
}

@Composable
private fun SettingsItemSelectLanguage(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentLanguage = NotepadApplication.currentLanguage
    val dropDownItems = remember { settingsViewModel.getLocalizationList(context).map { DropDownItem(text = it) } }
    val isExpanded = remember { mutableStateOf(false) }

    SettingsItem(
        modifier = Modifier.heightIn(min = 50.dp),
        icon = Icons.Outlined.Language,
        title = stringResource(R.string.ChoseLocalization),
        onClick = { isExpanded.value = true }
    ) {
        DropDownButton(
            items = dropDownItems,
            selectedIndex = currentLanguage.ordinal,
            expanded = isExpanded,
            stretchMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
            onChangedSelection = {
                settingsViewModel.selectLocalization(it)
                recreateActivity(context, coroutineScope)
            }) {
            androidx.compose.material3.Text(
                text = currentLanguage.getLocalizedValue(context),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = CustomTheme.colors.active
            )
        }
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