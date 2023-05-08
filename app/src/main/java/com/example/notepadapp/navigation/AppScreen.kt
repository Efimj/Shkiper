package com.example.notepadapp.navigation

sealed class AppScreen(val route: String) {
    object Welcome : AppScreen(route = "welcome_screen")
    object Home : AppScreen(route = "home_screen")
}
