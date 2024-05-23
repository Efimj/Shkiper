package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.screens.AppLayout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.layouts.*
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.delay
import me.onebone.toolbar.*

@Composable
fun ScreenCalendarContent(
    navController: NavController,
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    HideNavigation()

    AnimateVerticalSwitch(
        modifier = Modifier,
        state = viewModel.screenState.value.fullScreenCalendarOpen.not(),
        topComponent = {
            FullScreenCalendar(viewModel)
        }) {
        ScreenContent(collapsingToolbarScaffold, viewModel, onSlideBack, navController)
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
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

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
        if (viewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(
                modifier = Modifier.heightIn(max = 350.dp),
                title = R.string.NoReminders,
                icon = Icons.Outlined.NotificationsNone
            )
        else {
            NoteListContent(viewModel, currentRoute, navController)
        }
    }
}

@Composable
private fun NoteListContent(
    viewModel: CalendarViewModel,
    currentRoute: String,
    navController: NavController
) {
    val pinnedNotes =
        remember(viewModel.screenState.value.notes) { viewModel.screenState.value.notes.filter { it.isPinned } }
    val unpinnedNotes =
        remember(viewModel.screenState.value.notes) { viewModel.screenState.value.notes.filterNot { it.isPinned } }

    LazyGridNotes(
        contentPadding = PaddingValues(
            start = startWindowInsetsPadding() + 10.dp,
            top = 15.dp,
            end = endWindowInsetsPadding() + 10.dp,
            bottom = bottomWindowInsetsPadding() + 100.dp
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        noteTagsList(
            tags = viewModel.screenState.value.hashtags,
            selected = viewModel.screenState.value.currentHashtag
        ) { viewModel.setCurrentHashtag(it) }
        if (pinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Pinned)
            notesList(
                notes = pinnedNotes,
                reminders = viewModel.screenState.value.reminders,
                onClick = { note ->
                    viewModel.clickOnNote(
                        note = note,
                        currentRoute = currentRoute,
                        navController = navController
                    )
                },
            )
        }
        if (unpinnedNotes.isNotEmpty()) {
            notesListHeadline(headline = R.string.Other)
            notesList(
                notes = unpinnedNotes,
                reminders = viewModel.screenState.value.reminders,
                onClick = { note ->
                    viewModel.clickOnNote(
                        note = note,
                        currentRoute = currentRoute,
                        navController = navController
                    )
                },
            )
        }
    }
}