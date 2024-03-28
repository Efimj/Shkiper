package com.jobik.shkiper.screens.AppLayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.screens.AppLayout.NavigationBar.BottomAppBarProvider

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
    Box(modifier = Modifier.fillMaxSize()) {
        ScreenWrapper(
            navController = navController,
            startDestination = startDestination
        )
        BottomAppBarProvider(navController = navController)
        SnackbarProvider()
    }
}