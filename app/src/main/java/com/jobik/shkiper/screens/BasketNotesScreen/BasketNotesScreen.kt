package com.jobik.shkiper.screens.BasketNotesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.helpers.*
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.viewmodels.NotesViewModel


@Composable
fun BasketNotesScreen(navController: NavController, basketViewModel: NotesViewModel = hiltViewModel()) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val lazyGridNotes = rememberLazyStaggeredGridState()

    /**
     * When user select note
     */
    BackHandler(
        enabled = basketViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        basketViewModel::clearSelectedNote
    )

    Box(Modifier.fillMaxSize()) {
        if (basketViewModel.screenState.value.isNotesInitialized && basketViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.BasketNotesPageHeader, icon = Icons.Outlined.DeleteSweep)
        else
            ScreenContent(lazyGridNotes, basketViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            ActionBar(
                isVisible = basketViewModel.screenState.value.selectedNotes.isNotEmpty(),
                notesViewModel = basketViewModel
            )
        }
    }

    if (basketViewModel.screenState.value.isDeleteNotesDialogShow)
        ActionDialog(
            title = stringResource(R.string.DeleteSelectedNotesDialogText),
            icon = Icons.Outlined.Warning,
            confirmText = stringResource(R.string.Confirm),
            onConfirm = basketViewModel::deleteSelectedNotes,
            goBackText = stringResource(R.string.Cancel),
            onGoBack = basketViewModel::switchDeleteDialogShow
        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NotesViewModel,
    currentRoute: String,
    navController: NavController
) {
    LazyGridNotes(
        contentPadding = PaddingValues(
            start = 10.dp + startWindowInsetsPadding(),
            top = 10.dp + topWindowInsetsPadding(),
            end = 10.dp + endWindowInsetsPadding(),
            bottom = 80.dp + bottomWindowInsetsPadding()
        ),
        modifier = Modifier.fillMaxSize(),
        gridState = lazyGridNotes
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Column(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.BasketPageHeader),
                    color = CustomTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1
                )
            }
        }
        items(items = notesViewModel.screenState.value.notes) { item ->
            NoteCard(header = item.header,
                text = item.body,
                reminder = rememberNextReminder(
                    reminders = notesViewModel.screenState.value.reminders,
                    noteId = item._id,
                ),
                markedText = notesViewModel.screenState.value.searchText,
                selected = item._id in notesViewModel.screenState.value.selectedNotes,
                onClick = { notesViewModel.clickOnNote(item, currentRoute, navController) },
                onLongClick = { notesViewModel.toggleSelectedNoteCard(item._id) })
        }
    }
}

@Composable
private fun ActionBar(
    isVisible: Boolean, notesViewModel: NotesViewModel
) {
    CustomTopAppBar(
        isVisible = isVisible,
        counter = notesViewModel.screenState.value.selectedNotes.count(),
        navigation = TopAppBarItem(
            isActive = false,
            icon = Icons.Default.Close,
            iconDescription = R.string.GoBack,
            onClick = notesViewModel::clearSelectedNote
        ),
        items = listOf(
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.History,
                iconDescription = R.string.Restore,
                onClick = notesViewModel::removeSelectedNotesFromBasket
            ),
            TopAppBarItem(
                isActive = false,
                icon = Icons.Outlined.DeleteForever,
                iconDescription = R.string.Delete,
                onClick = notesViewModel::switchDeleteDialogShow
            ),
        )
    )
}

