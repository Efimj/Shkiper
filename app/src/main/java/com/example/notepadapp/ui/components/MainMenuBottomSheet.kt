package com.example.notepadapp.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.SetupHomePageNavGraph
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
fun MainMenuBottomSheet() {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberAnimatedNavController()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetBackgroundColor = CustomAppTheme.colors.mainBackground,
        sheetState = bottomSheetState,
        scrimColor = CustomAppTheme.colors.modalBackground,
        sheetShape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        sheetContent = {
            // Здесь вы можете определить свой макет BottomSheet
            BottomSheetContent(navController, coroutineScope, bottomSheetState)
        }
    ) {
        MainPageLayout(navController, coroutineScope, bottomSheetState)
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
private fun MainPageLayout(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SetupHomePageNavGraph(navController = navController, startDestination = UserPage.Notes.route)
        }
        Box(
            Modifier
                .align(Alignment.BottomCenter)
        ) {
            RoundedButton(
                text = "Menu",
                icon = navController.currentBackStackEntryAsState().value?.destination?.route?.let {
                    getCurrentMenuIcon(
                        it
                    )
                },
                onClick = {
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                },
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
                modifier = Modifier.height(37.dp).width(160.dp).offset(y = 1.dp)
            )
        }
    }
}

private fun getCurrentMenuIcon(currentRoute: String): ImageVector? {
    val currentButtonIcon = when (currentRoute) {
        UserPage.Notes.route ->
            Icons.Outlined.AutoAwesomeMosaic

        UserPage.Settings.route ->
            Icons.Outlined.Settings

        else -> null
    }
    return currentButtonIcon
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun BottomSheetContent(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MainMenuButton("Notes",
            Icons.Outlined.AutoAwesomeMosaic,
            isActive = currentRoute == UserPage.Notes.route,
            onClick = {
                goToPage(navController, UserPage.Notes.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Archive",
            Icons.Outlined.Archive,
            isActive = currentRoute == "",
            onClick = {
                goToPage(navController, UserPage.Settings.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Basket",
            Icons.Outlined.Delete,
            isActive = currentRoute == "",
            onClick = {
                goToPage(navController, UserPage.Settings.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Settings",
            Icons.Outlined.Settings,
            isActive = currentRoute == UserPage.Settings.route,
            onClick = {
                goToPage(navController, UserPage.Settings.route, coroutineScope, bottomSheetState)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun goToPage(
    navController: NavHostController,
    rout: String,
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    if (navController.currentDestination?.route == rout) {
        coroutineScope.launch { modalBottomSheetState.hide() }
        return
    }
    coroutineScope.launch { modalBottomSheetState.hide() }
    navController.navigate(rout)
}
