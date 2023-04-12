package com.example.notepadapp.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen(route = "welcome_screen")
    object AppSettings : Screen(route = "app_settings_screen")
    object Home : Screen(route = "home_screen")
}
