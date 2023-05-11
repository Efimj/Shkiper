package com.example.notepadapp.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.notepadapp.ui.components.bottomSheets.MainMenuBottomSheet
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
) {
        // Нижний лист, который будет отображаться при нажатии на кнопку
        MainMenuBottomSheet()
}





