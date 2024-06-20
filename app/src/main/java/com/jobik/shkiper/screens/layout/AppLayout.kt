package com.jobik.shkiper.screens.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.rememberNavController
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.navigation.SetupAppScreenNavGraph
import com.jobik.shkiper.screens.layout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.screens.layout.NavigationBar.BottomAppBarProvider
import com.jobik.shkiper.ui.theme.AppTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppLayout(startDestination: String = Route.NoteList.value) {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
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

        SetupAppScreenNavGraph(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(connection),
            navController = navController,
            startDestination = startDestination,
        )
        BottomAppBarProvider(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController,
        )
        SnackbarProvider()
    }
}