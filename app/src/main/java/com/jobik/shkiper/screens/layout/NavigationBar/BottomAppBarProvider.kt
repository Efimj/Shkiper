package com.jobik.shkiper.screens.layout.NavigationBar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.jobik.shkiper.screens.note.NoteScreen
import com.jobik.shkiper.ui.components.modals.FullscreenPopup
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.Startup
import kotlinx.coroutines.launch

@Composable
fun BottomAppBarProvider(
    navController: NavHostController,
) {
    val localDensity = LocalDensity.current
    var containerHeight by remember { mutableStateOf(0.dp) }
    val currentRouteName = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentRouteWithoutSecondaryRoutes =
        (navController.currentBackStackEntryAsState().value?.destination?.route
            ?: "").substringBefore("/")
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
                CreateNoteFAN(
                    navController = navController,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CreateNoteFAN(
    navController: NavHostController,
    viewModel: BottomBarViewModel = hiltViewModel<BottomBarViewModel>(),
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
            SharedTransitionLayout {
                var createReminderDialog by rememberSaveable { mutableStateOf(false) }

                AnimatedContent(
                    targetState = createReminderDialog,
                    label = "creation note"
                ) { state ->
                    if (state.not()) {
                        Surface(
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(
                                    key = "note-background-${Startup.paramNoteId}"
                                ),
                                animatedVisibilityScope = this@AnimatedContent,
                            ),
                            shape = MaterialTheme.shapes.small,
                            shadowElevation = 1.dp,
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .height(DefaultNavigationValues().containerHeight)
                                    .aspectRatio(1f)
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .background(AppTheme.colors.primary)
                                    .clickable {
                                        scope.launch {
                                            val noteId = viewModel.createNewNote()
                                            Startup.paramNoteId = noteId.toHexString()
                                            createReminderDialog = true
                                        }
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
                    } else {
                        FullscreenPopup(
                            onDismiss = {}
                        ) {
                            NoteScreen(
                                onBack = { createReminderDialog = false },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent,
                            )
                        }
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