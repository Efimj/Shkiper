package com.example.notepadapp.navigation

sealed class UserPage(val route: String) {
        object Settings: AppScreen(route = "settings_page")
        object Notes: AppScreen(route = "notes_page")
//        object AppSettingss: AppScreen(route = "app_settings_screen")
//        object Homes: AppScreen(route = "home_screen")
}
