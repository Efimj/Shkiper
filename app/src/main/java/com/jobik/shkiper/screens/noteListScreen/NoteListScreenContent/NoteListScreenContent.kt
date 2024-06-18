package com.jobik.shkiper.screens.noteListScreen.NoteListScreenContent

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Event
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.DonateBannerProvider
import com.jobik.shkiper.ui.components.fields.SearchBar
import com.jobik.shkiper.ui.components.fields.SearchBarActionButton
import com.jobik.shkiper.ui.components.fields.getSearchBarHeight
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.scrollConnectionToProvideVisibility
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.SupportTheDeveloperBannerUtil
import com.jobik.shkiper.viewmodels.NotesViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListScreenContent(
    navController: NavController,
    viewModel: NotesViewModel,
    onSlideNext: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val isSearchBarVisible = remember { mutableStateOf(true) }

    BackHandlerIfSelectedNotes(viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .scrollConnectionToProvideVisibility(visible = isSearchBarVisible)
    ) {
        Crossfade(
            targetState = viewModel.screenState.value.isNotesInitialized && viewModel.screenState.value.notes.isEmpty(),
            label = "animation layouts screen"
        ) { value ->
            if (value) {
                ScreenStub(title = R.string.EmptyNotesPageHeader, icon = Icons.Outlined.Description)
            } else {
                NotesListContent(
                    notesViewModel = viewModel,
                    navController = navController,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
        Box(modifier = Modifier) {
            SearchBar(
                isVisible = viewModel.screenState.value.selectedNotes.isEmpty() && isSearchBarVisible.value,
                value = viewModel.screenState.value.searchText,
                actionButton = SearchBarActionButton(
                    icon = Icons.Outlined.Event,
                    contentDescription = R.string.Reminders,
                    onClick = onSlideNext
                ),
                onChange = viewModel::changeSearchText,
            )
            NoteListScreenActionBar(
                isVisible = viewModel.screenState.value.selectedNotes.isNotEmpty(),
                notesViewModel = viewModel
            )
        }
    }

    NoteListScreenReminderCheck(viewModel)
    CreateReminderContent(viewModel)
}

@Composable
private fun BackHandlerIfSelectedNotes(notesViewModel: NotesViewModel) {
    /**
     * When user select note
     */
    BackHandler(
        enabled = notesViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        notesViewModel::clearSelectedNote
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun NotesListContent(
    notesViewModel: NotesViewModel,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val pinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filter { it.isPinned } }
    val unpinnedNotes =
        remember(notesViewModel.screenState.value.notes) { notesViewModel.screenState.value.notes.filterNot { it.isPinned } }

    val showDonateBanner =
        rememberSaveable { mutableStateOf(SupportTheDeveloperBannerUtil.isBannerNeeded(context)) }

    LazyGridNotes(
        contentPadding = PaddingValues(
            start = 10.dp + startWindowInsetsPadding(),
            top = getSearchBarHeight() + 10.dp,
            end = 10.dp + endWindowInsetsPadding(),
            bottom = 80.dp + bottomWindowInsetsPadding()
        ),
        modifier = Modifier
            .fillMaxSize()
            .testTag("notes_list"),
    ) {
        if (showDonateBanner.value) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    DonateBannerProvider(
                        isVisible = showDonateBanner,
                        navController = navController
                    )
                }
            }
        }
        noteTagsList(
            tags = notesViewModel.screenState.value.hashtags,
            selected = notesViewModel.screenState.value.currentHashtag
        ) { notesViewModel.setCurrentHashtag(it) }
        if (pinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Pinned)
            notesList(
                notes = pinnedNotes,
                reminders = notesViewModel.screenState.value.reminders,
                marker = notesViewModel.screenState.value.searchText,
                selected = notesViewModel.screenState.value.selectedNotes,
                onClick = { note ->
                    notesViewModel.clickOnNote(
                        note = note,
                        currentRoute = currentRoute,
                        navController = navController
                    )
                },
                onLongClick = { note ->
                    notesViewModel.toggleSelectedNoteCard(
                        noteId = note._id
                    )
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
        if (unpinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Other)
            notesList(
                notes = unpinnedNotes,
                reminders = notesViewModel.screenState.value.reminders,
                marker = notesViewModel.screenState.value.searchText,
                selected = notesViewModel.screenState.value.selectedNotes,
                onClick = { note ->
                    notesViewModel.clickOnNote(
                        note = note,
                        currentRoute = currentRoute,
                        navController = navController
                    )
                },
                onLongClick = { note ->
                    notesViewModel.toggleSelectedNoteCard(
                        noteId = note._id
                    )
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@Composable
private fun CreateReminderContent(notesViewModel: NotesViewModel) {
    if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
        CreateReminderDialog(
            reminderDialogProperties = ReminderDialogProperties(),
            onGoBack = notesViewModel::switchReminderDialogShow,
            onDelete = null,
            onSave = notesViewModel::createReminder,
        )
    }
}