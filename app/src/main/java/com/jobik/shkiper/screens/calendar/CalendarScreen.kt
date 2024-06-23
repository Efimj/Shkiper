package com.jobik.shkiper.screens.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.navigateToSecondary
import com.jobik.shkiper.navigation.Screen
import com.jobik.shkiper.screens.layout.navigation.AppNavigationBarState
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.delay
import me.onebone.toolbar.*

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    HideNavigation()

    AnimateVerticalSwitch(
        modifier = Modifier.background(AppTheme.colors.background),
        state = viewModel.screenState.value.fullScreenCalendarOpen.not(),
        topComponent = {
            FullScreenCalendar(viewModel)
        }) {
        ScreenContent(
            collapsingToolbarScaffold = collapsingToolbarScaffold,
            viewModel = viewModel,
            onSlideBack = {
                if (navController.canNavigate()) {
                    navController.popBackStack()
                }
            },
            navController = navController
        )
    }
}

@Composable
private fun HideNavigation() {
    LaunchedEffect(Unit) {
        delay(5)
        AppNavigationBarState.hideWithLock()
    }
}

@Composable
private fun ScreenContent(
    collapsingToolbarScaffold: CollapsingToolbarScaffoldState,
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
    navController: NavController
) {
    CollapsingToolbarScaffold(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .background(AppTheme.colors.background),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.EnterAlways,
        enabledWhenBodyUnfilled = false,
        snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
        toolbar = {
            ScreenCalendarTopBar(viewModel = viewModel, onSlideBack = onSlideBack)
        }
    ) {
        Crossfade(
            targetState = viewModel.screenState.value.isNotesInitialized && viewModel.screenState.value.notes.isEmpty(),
            label = "animation layouts screen"
        ) { value ->
            if (value)
                ScreenStub(
                    modifier = Modifier.heightIn(max = 350.dp),
                    title = R.string.NoReminders,
                    icon = Icons.Outlined.NotificationsNone
                )
            else {
                NoteListContent(
                    notes = viewModel.screenState.value.notes,
                    clickOnNote = { note ->
                        navController.navigateToSecondary(
                            Screen.Note(
                                id = note._id.toHexString(),
                                sharedElementOrigin = Screen.Calendar.name
                            )
                        )
                    },
                    tags = viewModel.screenState.value.hashtags,
                    selectedTag = viewModel.screenState.value.currentHashtag,
                    selectTag = viewModel::setCurrentHashtag,
                    reminders = viewModel.screenState.value.reminders,
                )
            }
        }
    }
}

@Composable
private fun NoteListContent(
    notes: List<Note>,
    clickOnNote: (Note) -> Unit,
    tags: Set<String>,
    selectedTag: String?,
    selectTag: (String) -> Unit,
    reminders: List<Reminder>
) {
    val pinnedNotes = remember(notes) { notes.filter { it.isPinned } }
    val unpinnedNotes = remember(notes) { notes.filterNot { it.isPinned } }

    LazyGridNotes(
        contentPadding = PaddingValues(
            start = startWindowInsetsPadding() + 10.dp,
            top = 10.dp,
            end = endWindowInsetsPadding() + 10.dp,
            bottom = bottomWindowInsetsPadding() + 100.dp
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        noteTagsList(
            tags = tags,
            selected = selectedTag,
            onSelect = selectTag
        )
        if (pinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Pinned)
            notesList(
                notes = pinnedNotes,
                reminders = reminders,
                onClick = clickOnNote,
            )
        }
        if (unpinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Other)
            notesList(
                notes = unpinnedNotes,
                reminders = reminders,
                onClick = clickOnNote,
            )
        }
    }
}