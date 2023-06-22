package com.example.notepadapp.screens.SettingsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.util.ThemeUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen() {
    val themePreferenceManager = ThemePreferenceManager(LocalContext.current)
    val themeModeIcon: ImageVector = if (ThemeUtil.isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode

    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
    {
        Column (Modifier.padding(20.dp, 65.dp)) {
            Text(
                color = CustomAppTheme.colors.text,
                text = "Settings",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp).align(Alignment.CenterHorizontally)
            )
            SettingsScreenMenuElement(
                "Application theme",
                ThemeUtil.isDarkTheme.let { if (it) "Light" else "Dark" },
                themeModeIcon,
                onClick = {
                    ThemeUtil.toggleTheme()
                    themePreferenceManager.saveTheme(ThemeUtil.isDarkTheme)
                }
            )
            SettingsScreenMenuElement(
                "Save all notes locally",
                "Save",
                Icons.Outlined.Download,
                {})
            SettingsScreenMenuElement(
                "Upload notes",
                "Upload",
                Icons.Outlined.Upload,
                {})
            SettingsScreenMenuElement(
                "Cloud storage",
                "Connect",
                Icons.Outlined.CloudOff,
                {})
            Text(
                color = CustomAppTheme.colors.text,
                text = "Information",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp)
                    .padding(top = 15.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = CustomAppTheme.colors.textSecondary
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "All your information is stored locally on your device, clearing your browsing data may result in losing all your data. Therefore, before resetting, save the file with your data so as not to lose them.",
                    color = CustomAppTheme.colors.textSecondary,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            //Spacer(modifier = Modifier.padding(bottom = 65.dp))
        }
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
                modifier = Modifier.align(Alignment.CenterEnd)
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

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    CustomAppTheme {
        SettingsScreen()
    }
}
