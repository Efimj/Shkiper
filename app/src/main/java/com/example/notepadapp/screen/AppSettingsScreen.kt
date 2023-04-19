package com.example.notepadapp.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.ui.components.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.util.ThemeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppSettingsScreen() {
    val themePreferenceManager = ThemePreferenceManager(LocalContext.current)
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(1) {
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 65.dp, bottom = 12.dp)
                )
                MyElement(
                    "Application theme",
                    ThemeUtil.isDarkTheme.let { if (it) "Light" else "Dark" },
                    null,
                    onClick = {
                        ThemeUtil.toggleTheme()
                        themePreferenceManager.saveTheme(ThemeUtil.isDarkTheme)
                    }
                )
                MyElement("Save all notes locally", "Save", null, {})
                MyElement("Upload notes", "Upload", null, {})
                MyElement("Cloud storage", "Connected", null, {})
                Text(
                    text = "Information",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                        .padding(top = 15.dp)
                )
                Text(
                    text = "All your information is stored locally on your device, clearing your browsing data may result in losing all your data. Therefore, before resetting, save the file with your data so as not to lose them.",
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.padding(bottom = 65.dp))
            }
        }
        Box(
            Modifier
                .align(Alignment.BottomCenter)
        ) {
            RoundedButton(
                text = "Text",
                onClick = {
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                },
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
                modifier = Modifier.height(35.dp).width(160.dp)
            )
        }
        // Нижний лист, который будет отображаться при нажатии на кнопку
        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            scrimColor = CustomAppTheme.colors.modalBackground,
            sheetShape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            sheetContent = {
                // Здесь вы можете определить свой макет BottomSheet
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                ) {
                    Text(text = "Привет, я BottomSheet!")
                }
            }
        ) {

        }
    }
}


@Composable
fun MyElement(
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
    AppSettingsScreen()
}
