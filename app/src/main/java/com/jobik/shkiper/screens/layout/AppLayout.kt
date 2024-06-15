package com.jobik.shkiper.screens.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.screens.layout.NavigationBar.BottomAppBarProvider
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun AppLayout(startDestination: String = Route.NoteList.route) {
    val navController = rememberNavController()

    ScreenLayout(navController, startDestination)
}

@Composable
private fun ScreenLayout(
    navController: NavHostController,
    startDestination: String = Route.NoteList.route,
) {
    Box(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {
        ScreenWrapper(
            navController = navController,
            startDestination = startDestination
        )
        BottomAppBarProvider(navController = navController)
        SnackbarProvider()
    }
}