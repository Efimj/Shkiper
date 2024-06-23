package com.jobik.shkiper.screens.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.navigation.Screen
import com.jobik.shkiper.navigation.SetupAppScreenNavGraph
import com.jobik.shkiper.screens.layout.navigation.AppNavigationBarState
import com.jobik.shkiper.screens.layout.navigation.BottomAppBarProvider
import com.jobik.shkiper.ui.helpers.LocalNotePosition
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun AppLayout(startDestination: Screen) {
    Box(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {
        BottomAppBarProvider(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { navController, notePosition ->
            ScreenContent(navController, startDestination, notePosition)
        }
        SnackBarProvider()
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun ScreenContent(
    navController: NavHostController,
    startDestination: Screen,
    notePosition: NotePosition
) {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (consumed.y < -30) {
                    AppNavigationBarState.hide()
                }
                if (consumed.y > 30) {
                    AppNavigationBarState.show()
                }
                if (available.y > 0) {
                    AppNavigationBarState.show()
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    CompositionLocalProvider(
        LocalNotePosition provides notePosition,
    ) {
        SetupAppScreenNavGraph(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(connection),
            navController = navController,
            startDestination = startDestination,
        )
    }
}