package com.example.notepadapp.ui.components.modals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.SetupHomePageNavGraph
import com.example.notepadapp.navigation.UserPages
import com.example.notepadapp.ui.components.buttons.MainMenuButton
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
fun MainMenuBottomSheet(startDestination: String = UserPages.NoteList.route) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberAnimatedNavController()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    ModalBottomSheetLayout(
        sheetBackgroundColor = CustomAppTheme.colors.mainBackground,
        sheetState = bottomSheetState,
        scrimColor = CustomAppTheme.colors.modalBackground,
        sheetShape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        sheetContent = {
            BottomSheetContent(navController, coroutineScope, bottomSheetState)
        }
    ) {
        MainPageLayout(navController, startDestination, coroutineScope, bottomSheetState)
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
private fun MainPageLayout(
    navController: NavHostController,
    startDestination: String = UserPages.NoteList.route,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val isButtonHide = currentRoute.substringBefore("/") == UserPages.Note.route.substringBefore("/")
    val menuContainerHeight = 37

    val offsetY by animateDpAsState(
        if (isButtonHide) (menuContainerHeight).dp else 0.dp,
        animationSpec = TweenSpec(durationMillis = 300)
    )

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SetupHomePageNavGraph(navController = navController, startDestination = UserPages.NoteList.route)
        }
        Box(
            Modifier
                .offset(y = offsetY)
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
                modifier = Modifier.height(menuContainerHeight.dp).width(160.dp).offset(y = 1.dp)
            )
        }
    }
    val isInitialized = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (isInitialized.value) return@LaunchedEffect
        isInitialized.value = true
        if (startDestination != UserPages.NoteList.route)
            navController.navigate(startDestination)
    }
}

private fun getCurrentMenuIcon(currentRoute: String): ImageVector {
    val currentButtonIcon = when (currentRoute) {
        UserPages.NoteList.route ->
            Icons.Outlined.AutoAwesomeMosaic

        UserPages.Archive.route ->
            Icons.Outlined.Archive

        UserPages.Basket.route ->
            Icons.Outlined.Delete

        UserPages.Settings.route ->
            Icons.Outlined.Settings

        else -> Icons.Outlined.Menu
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
            isActive = currentRoute == UserPages.NoteList.route,
            onClick = {
                goToPage(navController, UserPages.NoteList.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Archive",
            Icons.Outlined.Archive,
            isActive = currentRoute == UserPages.Archive.route,
            onClick = {
                goToPage(navController, UserPages.Archive.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Basket",
            Icons.Outlined.Delete,
            isActive = currentRoute == UserPages.Basket.route,
            onClick = {
                goToPage(navController, UserPages.Basket.route, coroutineScope, bottomSheetState)
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton("Settings",
            Icons.Outlined.Settings,
            isActive = currentRoute == UserPages.Settings.route,
            onClick = {
                goToPage(navController, UserPages.Settings.route, coroutineScope, bottomSheetState)
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
