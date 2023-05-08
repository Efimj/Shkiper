package com.example.notepadapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notepadapp.navigation.AppScreen
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.theme.CustomAppTheme

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainMenuBottomSheet(bottomSheetState: ModalBottomSheetState, navController: NavHostController) {
    var lastButtonPressed by remember { mutableStateOf("Settings") }

    ModalBottomSheetLayout(
        sheetBackgroundColor = CustomAppTheme.colors.mainBackground,
        sheetState = bottomSheetState,
        scrimColor = CustomAppTheme.colors.modalBackground,
        sheetShape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        sheetContent = {
            // Здесь вы можете определить свой макет BottomSheet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MainMenuButton("Notes", isActive = lastButtonPressed == "Notes", onClick = {
                    goToPage(navController, UserPage.Settings.route)
                    lastButtonPressed = "Notes"
                })
                Spacer(modifier = Modifier.height(8.dp))
                MainMenuButton("Archive", isActive = lastButtonPressed == "Archive", onClick = {
                    goToPage(navController, UserPage.Settings.route)
                    lastButtonPressed = "Archive"
                })
                Spacer(modifier = Modifier.height(8.dp))
                MainMenuButton("Basket", isActive = lastButtonPressed == "Basket", onClick = {
                    goToPage(navController, UserPage.Settings.route)
                    lastButtonPressed = "Basket"
                })
                Spacer(modifier = Modifier.height(8.dp))
                MainMenuButton("Settings", isActive = lastButtonPressed == "Settings", onClick = {
                    goToPage(navController, UserPage.Settings.route)
                    lastButtonPressed = "Settings"
                    }
                )
            }
        }
    ) {
    }
}

private fun goToPage(navController: NavHostController, rout: String){
    navController.popBackStack()
    navController.navigate(rout)
}
