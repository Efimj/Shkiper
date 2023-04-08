package com.example.notepadapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.notepadapp.navigation.Screen
import com.example.notepadapp.navigation.SetupNavGraph
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.viewmodel.ThemeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { ThemeViewModel() }
            CustomAppTheme(darkTheme = viewModel.isDarkTheme.value) {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = Screen.Welcome.route)
            }
        }
    }
}
