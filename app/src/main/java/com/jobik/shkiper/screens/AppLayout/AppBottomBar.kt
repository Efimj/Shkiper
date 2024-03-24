package com.jobik.shkiper.screens.AppLayout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun BoxScope.BottomAppBar(
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

    val offsetY by animateDpAsState(
        if (AppNavigationBarState.isVisible.value) 0.dp else containerHeight,
        animationSpec = spring(stiffness = Spring.StiffnessMedium), label = "offsetY"
    )

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                // Set screen height using the LayoutCoordinates
                containerHeight = with(localDensity) { coordinates.size.height.toDp() }
            }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
        ) {
            Navigation(
                navController = navController
            )
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
                            .clickable {

                            },
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