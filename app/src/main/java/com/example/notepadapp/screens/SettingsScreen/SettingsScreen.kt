package com.example.notepadapp.screens.SettingsScreen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.example.notepadapp.NotepadApplication
import com.example.notepadapp.R
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.helpers.localization.LocaleHelper
import com.example.notepadapp.helpers.localization.Localization
import com.example.notepadapp.navigation.AppScreens
import com.example.notepadapp.ui.components.buttons.DropDownButton
import com.example.notepadapp.ui.components.buttons.DropDownButtonSizeMode
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.theme.ColorTheme
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.ui.theme.UserTheme
import com.example.notepadapp.util.ThemeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    val themePreferenceManager = ThemePreferenceManager(LocalContext.current)

    Column(
        Modifier.fillMaxSize().padding(horizontal = 20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(65.dp))
        Text(
            color = CustomAppTheme.colors.text,
            text = stringResource(R.string.Application),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
        )
        SettingsScreenMenuElement(
            stringResource(R.string.ApplicationTheme),
            ThemeUtil.currentTheme.let { if (it.isDarkTheme) stringResource(R.string.LightTheme) else stringResource(R.string.DarkTheme) },
            buttonIcon = if (ThemeUtil.currentTheme.isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            onClick = {
                ThemeUtil.toggleTheme()
                themePreferenceManager.saveTheme(
                    UserTheme(
                        !ThemeUtil.currentTheme.isDarkTheme,
                        if (ThemeUtil.currentTheme.isDarkTheme) ColorTheme.DefaultColorTheme.lightColors else ColorTheme.DefaultColorTheme.darkColors
                    )
                )
            }
        )
        SettingsScreenSelectLanguageElement()
        Text(
            color = CustomAppTheme.colors.text,
            text = stringResource(R.string.Backup),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
        )
        SettingsScreenMenuElement(
            stringResource(R.string.SaveAllNotesLocally),
            stringResource(R.string.Save),
            Icons.Outlined.Download,
            {})
        SettingsScreenMenuElement(
            stringResource(R.string.UploadNotes),
            stringResource(R.string.Upload),
            Icons.Outlined.Upload,
            {})
        SettingsScreenMenuElement(
            stringResource(R.string.CloudStorage),
            stringResource(R.string.Connect),
            Icons.Outlined.CloudOff,
            {})
        Text(
            color = CustomAppTheme.colors.text,
            text = stringResource(R.string.Other),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
        )
        SettingsScreenMenuElement(
            stringResource(R.string.OnboardingPage),
            stringResource(R.string.Open),
            Icons.Outlined.ViewCarousel
        ) { navController.navigate(AppScreens.Onboarding.route) }
        Text(
            color = CustomAppTheme.colors.text,
            text = stringResource(R.string.Information),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 15.dp)
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
        Spacer(Modifier.height(55.dp))
    }
}

@Composable
private fun SettingsScreenSelectLanguageElement() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentLanguage = NotepadApplication.currentLanguage
    val localizationList = Localization.values().map { it.getLocalizedValue(LocalContext.current) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                color = CustomAppTheme.colors.text,
                text = stringResource(R.string.ChoseLocalization),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            DropDownButton(localizationList,
                currentLanguage.ordinal,
                Modifier.width(150.dp),
                DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH,
                onChangedSelection = {
                    changeLocalization(it, context, scope)
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
    }
}

private fun changeLocalization(it: Int, context: Context, scope: CoroutineScope) {
    val newLanguage = Localization.values()[it]
    LocaleHelper.setLocale(context, newLanguage)

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
private fun SettingsScreenMenuElement(
    description: String,
    buttonText: String? = null,
    buttonIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
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
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            RoundedButton(
                text = buttonText,
                icon = buttonIcon,
                modifier = Modifier.width(145.dp),
                onClick = onClick,
            )
        }
    }
}
