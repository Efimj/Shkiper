package com.jobik.shkiper.screens.AppLayout

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.util.MainMenuButtonState

@Composable
fun AppLayout(startDestination: String = AppScreens.NoteList.route) {
    val navController = rememberNavController()

    ScreenLayout(navController, startDestination)
}

@Composable
private fun ScreenLayout(
    navController: NavHostController,
    startDestination: String = AppScreens.NoteList.route,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val navigationContainerHeight by remember { mutableStateOf(0.dp) }

    val currentRouteWithoutSubroutes =
        (navController.currentBackStackEntryAsState().value?.destination?.route ?: "").substringBefore("/")

    LaunchedEffect(currentRouteWithoutSubroutes) {
        MainMenuButtonState.isButtonOpened.value =
            AppScreens.SecondaryRoutes.isSecondaryRoute(currentRouteWithoutSubroutes)
    }

    val offsetY by animateDpAsState(
        if (MainMenuButtonState.isButtonOpened.value) (navigationContainerHeight) else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium), label = "offsetY"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationScreenContainer(navController, startDestination)
        AppBottomBar(offsetY, navController, currentRoute)
        SnackbarProvider()
    }
}