package com.jobik.shkiper.screens.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import com.jobik.shkiper.navigation.SetupAppScreenNavGraph
import com.jobik.shkiper.screens.layout.NavigationBar.AppNavigationBarState

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun ScreenWrapper(navController: NavHostController, destination: String) {

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection)
    ) {
        SetupAppScreenNavGraph(
            navController = navController,
            startDestination = destination,
        )
    }
}