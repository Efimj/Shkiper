package com.jobik.shkiper.screens.AppLayout.NavigationBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BoxScope.BottomAppBarProvider(
    navController: NavHostController,
) {
    val localDensity = LocalDensity.current
    var containerHeight by remember { mutableStateOf(0.dp) }
    val currentRouteName = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentRouteWithoutSecondaryRoutes =
        (navController.currentBackStackEntryAsState().value?.destination?.route ?: "").substringBefore("/")
    val isSecondaryScreen = AppScreens.SecondaryRoutes.isSecondaryRoute(currentRouteWithoutSecondaryRoutes)

    LaunchedEffect(currentRouteName) {
        if (isSecondaryScreen) {
            AppNavigationBarState.hideWithLock()
        } else {
            AppNavigationBarState.showWithUnlock()
        }
    }

    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = AppNavigationBarState.isVisible.value,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // Set screen height using the LayoutCoordinates
                    containerHeight = with(localDensity) { coordinates.size.height.toDp() }
                }
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0F to CustomTheme.colors.mainBackground.copy(alpha = 0.0F),
                        0.8F to CustomTheme.colors.mainBackground.copy(alpha = 1F)
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
            ) {
                Navigation(
                    navController = navController
                )
                CreateNoteFAN(navController = navController)
            }
        }
    }
}

@Composable
private fun RowScope.CreateNoteFAN(
    navController: NavHostController,
    viewModel: BottomBarViewModel = hiltViewModel<BottomBarViewModel>()
) {
    val currentRouteName = navController.currentBackStackEntryAsState().value?.destination?.route
    val scope = rememberCoroutineScope()

    AnimatedVisibility(visible = currentRouteName == AppScreens.NoteList.route) {
        Row(modifier = Modifier.height(DefaultNavigationValues().containerHeight)) {
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(shape = MaterialTheme.shapes.small)
                    .border(
                        width = 1.dp,
                        color = CustomTheme.colors.mainBackground,
                        shape = MaterialTheme.shapes.small
                    )
                    .background(CustomTheme.colors.active)
                    .clickable { createNewNote(scope = scope, viewModel = viewModel, navController = navController) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.CreateNote),
                    tint = CustomTheme.colors.textOnActive,
                )
            }
        }
    }
}

private fun createNewNote(
    scope: CoroutineScope,
    viewModel: BottomBarViewModel,
    navController: NavHostController
) {
    scope.launch {
        val noteId = viewModel.createNewNote()
        navController.navigate(AppScreens.Note.noteId(noteId.toHexString()))
    }
}

@Composable
private fun Navigation(
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

    CustomBottomNavigation(navigationItems)
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