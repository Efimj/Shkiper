package com.example.notepadapp.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notepadapp.screen.NotesPage
import com.example.notepadapp.screen.SettingsPage
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupHomePageNavGraph(
    navController: NavHostController,
    startDestination: String
)  {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = UserPage.Settings.route) {
            EnterAnimation {
                SettingsPage()
            }
        }
        composable(route = UserPage.Notes.route) {
            EnterAnimation {
                NotesPage()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}
