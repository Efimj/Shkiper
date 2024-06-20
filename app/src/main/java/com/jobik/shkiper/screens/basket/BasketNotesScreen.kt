package com.jobik.shkiper.screens.basket

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
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
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.navigateToSecondary
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.components.modals.ActionDialog
import com.jobik.shkiper.ui.helpers.*
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.viewmodels.NotesViewModel

@Composable
fun BasketNotesScreen(
    navController: NavController,
    basketViewModel: NotesViewModel = hiltViewModel()
) {
    val lazyGridNotes = rememberLazyStaggeredGridState()

    /**
     * When user select note
     */
    BackHandler(
        enabled = basketViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        basketViewModel::clearSelectedNote
    )

    Box(Modifier.fillMaxSize()) {
        Crossfade(
            targetState = basketViewModel.screenState.value.isNotesInitialized && basketViewModel.screenState.value.notes.isEmpty(),
            label = "animation layouts screen"
        ) { value ->
            if (value) {
                ScreenStub(
                    title = R.string.BasketNotesPageHeader,
                    icon = Icons.Outlined.DeleteSweep
                )
            } else {
                ScreenContent(
                    lazyGridNotes = lazyGridNotes,
                    notesViewModel = basketViewModel,
                    navController = navController
                )
            }
        }
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

@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    val sharedOrigin = LocalSharedElementKey.current

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
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1
                )
            }
        }
        notesList(
            notes = notesViewModel.screenState.value.notes,
            reminders = notesViewModel.screenState.value.reminders,
            marker = notesViewModel.screenState.value.searchText,
            selected = notesViewModel.screenState.value.selectedNotes,
            onClick = { note ->
                notesViewModel.clickOnNote(
                    note = note,
                    onNavigate = {
                        navController.navigateToSecondary(
                            Route.Note.configure(
                                id = note._id.toHexString(),
                                sharedElementOrigin = sharedOrigin
                            )
                        )
                    })
            },
            onLongClick = { note ->
                notesViewModel.toggleSelectedNoteCard(noteId = note._id)
            },
        )
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

