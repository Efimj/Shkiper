package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun NoteScreen(navController: NavController, noteViewModel: NoteViewModel = hiltViewModel()) {
    val systemUiController = rememberSystemUiController()

    NoteScreenContent(noteViewModel, navController)
    RemindersContent(noteViewModel)

    val secondaryBackgroundColor = CustomTheme.colors.secondaryBackground
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setNavigationBarColor(secondaryBackgroundColor)
        }
    }

    LeaveScreenIfNeeded(noteViewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemindersContent(noteViewModel: NoteViewModel) {
    val context = LocalContext.current
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(noteViewModel.screenState.value.isCreateReminderDialogShow) {
        if (!noteViewModel.screenState.value.isCreateReminderDialogShow) {
            shareSheetState.hide()
        }
    }

    if (noteViewModel.screenState.value.isCreateReminderDialogShow) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchReminderDialogShow()
            },
            dragHandle = null,
        ) {

        }
    }
}

@Composable
private fun LeaveScreenIfNeeded(
    noteViewModel: NoteViewModel,
    navController: NavController
) {
    LaunchedEffect(noteViewModel.screenState.value.isGoBack) {
        noteViewModel.runFetchingLinksMetaData()
        if (noteViewModel.screenState.value.isGoBack) navController.popBackStack()
    }
}