package com.jobik.shkiper.screens.layout.NavigationBar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.navigateToMain
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.navigation.RouteHelper
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.AppTheme
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
    val isSecondaryScreen = RouteHelper().isSecondaryRoute(currentRouteWithoutSecondaryRoutes)

    LaunchedEffect(currentRouteName) {
        if (isSecondaryScreen) {
            AppNavigationBarState.hideWithLock()
        } else {
            AppNavigationBarState.showWithUnlock()
        }
    }

    val isKeyboardVisible by keyboardAsState()

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible.name == Keyboard.Opened.name) {
            AppNavigationBarState.hide()
        } else {
            AppNavigationBarState.show()
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
                        0F to AppTheme.colors.background.copy(alpha = 0.0F),
                        0.8F to AppTheme.colors.background.copy(alpha = 1F)
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                    .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.zIndex(2f)) {
                    Navigation(
                        navController = navController
                    )
                }
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

    AnimatedVisibility(
        visible = currentRouteName == Route.NoteList.route,
        enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
        exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
    ) {
        Row {
            Spacer(modifier = Modifier.width(10.dp))
            Surface(shape = MaterialTheme.shapes.small, shadowElevation = 1.dp, color = Color.Transparent) {
                Row(
                    modifier = Modifier
                        .height(DefaultNavigationValues().containerHeight)
                        .aspectRatio(1f)
                        .clip(shape = MaterialTheme.shapes.small)
                        .background(AppTheme.colors.primary)
                        .clickable {
                            createNewNote(
                                scope = scope,
                                viewModel = viewModel,
                                navController = navController
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.CreateNote),
                        tint = AppTheme.colors.onPrimary,
                    )
                }
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
        navController.navigateToMain(Route.Note.noteId(noteId.toHexString()))
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
            isSelected = currentRoute == Route.NoteList.route,
            description = R.string.Notes,
        ) {
            navController.navigateToMain(destination = Route.NoteList.notePosition(NotePosition.MAIN.name))
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Archive,
            isSelected = currentRoute == Route.Archive.route,
            description = R.string.Archive,
        ) {
            navController.navigateToMain(destination = Route.Archive.notePosition(NotePosition.ARCHIVE.name))
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Delete,
            isSelected = currentRoute == Route.Basket.route,
            description = R.string.Basket,
        ) {
            navController.navigateToMain(destination = Route.Basket.notePosition(NotePosition.DELETE.name))
        },
        CustomBottomNavigationItem(
            icon = Icons.Outlined.Settings,
            isSelected = currentRoute == Route.Settings.route,
            description = R.string.Settings
        ) {
            navController.navigateToMain(destination = Route.Settings.route)
        },
    )

    CustomBottomNavigation(navigationItems)
}