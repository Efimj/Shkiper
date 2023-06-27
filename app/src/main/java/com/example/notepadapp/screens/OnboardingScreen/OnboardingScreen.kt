package com.example.notepadapp.screens.OnboardingScreen

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.notepadapp.navigation.AppScreens

@Composable
fun OnboardingScreen(navController: NavController) {

    Button({navController.navigate(AppScreens.NoteList.route)}){
        Text("AppScreens")
    }
}