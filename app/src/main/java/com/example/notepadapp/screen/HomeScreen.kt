package com.example.notepadapp.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notepadapp.navigation.SetupHomePageNavGraph
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.components.MainMenuBottomSheet
import com.example.notepadapp.ui.components.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
) {
        // Нижний лист, который будет отображаться при нажатии на кнопку
        MainMenuBottomSheet()
}





