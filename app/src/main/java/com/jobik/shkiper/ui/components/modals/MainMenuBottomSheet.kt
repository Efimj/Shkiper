package com.jobik.shkiper.ui.components.modals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.SetupAppScreenNavGraph
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.screens.AppLayout.CustomBottomNavigation
import com.jobik.shkiper.screens.AppLayout.CustomBottomNavigationItem
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.MainMenuButtonState
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom

@Composable
fun AppLayout(startDestination: String = AppScreens.NoteList.route) {
    val navController = rememberNavController()

    MainPageLayout(navController, startDestination)
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun MainPageLayout(
    navController: NavHostController,
    startDestination: String = AppScreens.NoteList.route,
) {
    val localDensity = LocalDensity.current
    var navigationContainerHeight by remember { mutableStateOf(0.dp) }

    val currentRoute =
        (navController.currentBackStackEntryAsState().value?.destination?.route ?: "").substringBefore("/")

    LaunchedEffect(currentRoute) {
        MainMenuButtonState.isButtonOpened.value = AppScreens.SecondaryRoutes.isSecondaryRoute(currentRoute)
    }

    val offsetY by animateDpAsState(
        if (MainMenuButtonState.isButtonOpened.value) (navigationContainerHeight) else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium), label = "offsetY"
    )

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SetupAppScreenNavGraph(
                navController = navController,
                startDestination = if (startDestination == AppScreens.Onboarding.route) AppScreens.Onboarding.route else AppScreens.NoteList.route
            )
        }
        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0F to CustomTheme.colors.mainBackground.copy(alpha = 0.0F),
                        0.8F to CustomTheme.colors.mainBackground.copy(alpha = 1F)
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Navigation(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        // Set screen height using the LayoutCoordinates
                        navigationContainerHeight = with(localDensity) { coordinates.size.height.toDp() }
                    },
                navController = navController
            )
        }
        SnackbarHost(
            hostState = SnackbarHostUtil.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Box(
                Modifier
                    .offset(y = -navigationContainerHeight)
                    .align(Alignment.BottomCenter)
            ) {
                val customVisuals = snackbarData.visuals as SnackbarVisualsCustom
                SnackbarCard(customVisuals)
            }
        }
    }
    val isInitialized = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (isInitialized.value) return@LaunchedEffect
        isInitialized.value = true
        if (startDestination != AppScreens.NoteList.notePosition(NotePosition.MAIN.name) && startDestination != AppScreens.Onboarding.route)
            navController.navigate(startDestination) {
                launchSingleTop
            }
    }
}

@Composable
private fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val navigationItems = listOf(
        CustomBottomNavigationItem(
            icon = Icons.Outlined.AutoAwesomeMosaic,
            isSelected = currentRoute == AppScreens.NoteList.route,
            description = R.string.Notes,
        ) {
            goToPage(
                navController = navController,
                rout = AppScreens.NoteList.notePosition(NotePosition.MAIN.name)
            )
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Archive,
            isSelected = currentRoute == AppScreens.Archive.route,
            description = R.string.Archive,
        ) {
            goToPage(
                navController = navController,
                rout = AppScreens.Archive.notePosition(NotePosition.ARCHIVE.name),
            )
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Delete,
            isSelected = currentRoute == AppScreens.Basket.route,
            description = R.string.Basket,
        ) {
            goToPage(
                navController = navController,
                rout = AppScreens.Basket.notePosition(NotePosition.DELETE.name),
            )
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Settings,
            isSelected = currentRoute == AppScreens.Settings.route,
            description = R.string.Settings
        ) {
            goToPage(
                navController = navController,
                rout = AppScreens.Settings.route,
            )
        },
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
    ) {
        CustomBottomNavigation(navigationItems)
    }
}

private fun goToPage(
    navController: NavHostController,
    rout: String,
) {
    if (navController.canNavigate()
            .not() || navController.currentDestination?.route?.substringBefore("/") == rout.substringBefore("/")
    ) {
        return
    }
    navController.navigate(rout) {
        launchSingleTop
    }
}
