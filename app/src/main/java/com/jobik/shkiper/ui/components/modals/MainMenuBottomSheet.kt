package com.jobik.shkiper.ui.components.modals

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.SetupAppScreenNavGraph
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.ButtonProperties
import com.jobik.shkiper.ui.components.buttons.MainMenuButton
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.components.buttons.DefaultButtonProperties
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainMenuBottomSheet(startDestination: String = AppScreens.NoteList.route) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    ModalBottomSheetLayout(
        sheetBackgroundColor = CustomTheme.colors.secondaryBackground,
        sheetState = bottomSheetState,
        scrimColor = CustomTheme.colors.modalBackground,
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
    startDestination: String = AppScreens.NoteList.route,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    val currentRoute =
        (navController.currentBackStackEntryAsState().value?.destination?.route ?: "").substringBefore("/")
    val isButtonHide = AppScreens.SecondaryRoutes.isSecondaryRoute(currentRoute)
    val menuContainerHeight = 40

    val offsetY by animateDpAsState(
        if (isButtonHide) (menuContainerHeight).dp else 0.dp,
        animationSpec = TweenSpec(durationMillis = 200)
    )

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SetupAppScreenNavGraph(
                navController = navController,
                startDestination = if (startDestination == AppScreens.Onboarding.route) AppScreens.Onboarding.route else AppScreens.NoteList.route
            )
        }
        SnackbarHost(
            hostState = SnackbarHostUtil.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Box(
                Modifier.offset(y = (-30).dp).align(Alignment.BottomCenter)
            ) {
                val customVisuals = snackbarData.visuals as SnackbarVisualsCustom
                SnackbarCard(customVisuals)
            }
        }
        Box(
            modifier = Modifier.offset(y = offsetY).align(Alignment.BottomCenter).fillMaxWidth().background(
                Brush.verticalGradient(
                    0F to CustomTheme.colors.mainBackground.copy(alpha = 0.0F),
                    0.8F to CustomTheme.colors.mainBackground.copy(alpha = 1F)
                )
            ),
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomButton(
                text = getCurrentMenuText(currentRoute),
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
                properties = DefaultButtonProperties(
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomTheme.colors.secondaryBackground,
                        disabledBackgroundColor = Color.Transparent
                    ),
                    iconTint = CustomTheme.colors.text,
                    textColor = CustomTheme.colors.text,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    horizontalPaddings = 20.dp,
                    border = BorderStroke(width = 1.dp, color = CustomTheme.colors.stroke)
                ),
                modifier = Modifier.height(menuContainerHeight.dp).width(160.dp).offset(y = 1.dp),
            )
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

private fun getCurrentMenuIcon(currentRoute: String): ImageVector {
    val currentButtonIcon = when (currentRoute.substringBefore("/")) {
        AppScreens.NoteList.route.substringBefore("/") ->
            Icons.Outlined.AutoAwesomeMosaic

        AppScreens.Archive.route.substringBefore("/") ->
            Icons.Outlined.Archive

        AppScreens.Basket.route.substringBefore("/") ->
            Icons.Outlined.Delete

        AppScreens.Settings.route.substringBefore("/") ->
            Icons.Outlined.Settings

        else -> Icons.Outlined.Menu
    }
    return currentButtonIcon
}

@Composable
private fun getCurrentMenuText(currentRoute: String): String {
    val currentButtonIcon = when (currentRoute.substringBefore("/")) {
        AppScreens.NoteList.route.substringBefore("/") ->
            stringResource(R.string.Notes)

        AppScreens.Archive.route.substringBefore("/") ->
            stringResource(R.string.Archive)

        AppScreens.Basket.route.substringBefore("/") ->
            stringResource(R.string.Basket)

        AppScreens.Settings.route.substringBefore("/") ->
            stringResource(R.string.Settings)

        else -> stringResource(R.string.Menu)
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
        MainMenuButton(stringResource(R.string.Notes),
            Icons.Outlined.AutoAwesomeMosaic,
            isActive = currentRoute == AppScreens.NoteList.route,
            onClick = {
                goToPage(
                    navController,
                    AppScreens.NoteList.notePosition(NotePosition.MAIN.name),
                    coroutineScope,
                    bottomSheetState
                )
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton(stringResource(R.string.Archive),
            Icons.Outlined.Archive,
            isActive = currentRoute == AppScreens.Archive.route,
            onClick = {
                goToPage(
                    navController,
                    AppScreens.Archive.notePosition(NotePosition.ARCHIVE.name),
                    coroutineScope,
                    bottomSheetState
                )
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton(stringResource(R.string.Basket),
            Icons.Outlined.Delete,
            isActive = currentRoute == AppScreens.Basket.route,
            onClick = {
                goToPage(
                    navController,
                    AppScreens.Basket.notePosition(NotePosition.DELETE.name),
                    coroutineScope,
                    bottomSheetState
                )
            })
        Spacer(modifier = Modifier.height(8.dp))
        MainMenuButton(stringResource(R.string.Settings),
            Icons.Outlined.Settings,
            isActive = currentRoute == AppScreens.Settings.route,
            onClick = {
                goToPage(
                    navController,
                    AppScreens.Settings.route,
                    coroutineScope,
                    bottomSheetState
                )
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
    if (navController.currentDestination?.route?.substringBefore("/") == rout.substringBefore("/")) {
        coroutineScope.launch { modalBottomSheetState.hide() }
        return
    }
    coroutineScope.launch { modalBottomSheetState.hide() }
    navController.navigate(rout) {
        launchSingleTop
    }
}
