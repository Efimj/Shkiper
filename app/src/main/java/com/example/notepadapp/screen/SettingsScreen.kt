package com.example.notepadapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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
import com.example.notepadapp.ui.components.MainPageWithBottomSheetMenu
import com.example.notepadapp.ui.components.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.util.ThemeUtil


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen() {
    val themePreferenceManager = ThemePreferenceManager(LocalContext.current)

    Box(Modifier.fillMaxSize().background(CustomAppTheme.colors.mainBackground)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(1) {
                Text(
                    color = CustomAppTheme.colors.text,
                    text = "Settings",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 65.dp, bottom = 12.dp)
                )
                SettingsPageMenuElement(
                    "Application theme",
                    ThemeUtil.isDarkTheme.let { if (it) "Light" else "Dark" },
                    null,
                    onClick = {
                        ThemeUtil.toggleTheme()
                        themePreferenceManager.saveTheme(ThemeUtil.isDarkTheme)
                    }
                )
                SettingsPageMenuElement("Save all notes locally", "Save", null, {})
                SettingsPageMenuElement("Upload notes", "Upload", null, {})
                SettingsPageMenuElement("Cloud storage", "Connected", null, {})
                Text(
                    color = CustomAppTheme.colors.text,
                    text = "Information",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                        .padding(top = 15.dp)
                )
                Text(
                    text = "All your information is stored locally on your device, clearing your browsing data may result in losing all your data. Therefore, before resetting, save the file with your data so as not to lose them.",
                    color = CustomAppTheme.colors.textSecondary,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.padding(bottom = 65.dp))
            }
        }
    }
}

@Composable
private fun SettingsPageMenuElement(
    description: String,
    buttonText: String? = null,
    buttonIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
    CustomAppTheme{
        SettingsScreen()
    }
}
