package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.screens.AppLayout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.helpers.startWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.delay
import me.onebone.toolbar.*
import java.time.LocalDateTime
import java.time.LocalTime

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
        if (viewModel.screenState.value.hashtags.isNotEmpty())
            item(span = StaggeredGridItemSpan.FullLine) {
                LazyRow(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = viewModel.screenState.value.hashtags.toList()) { item ->
                        HashtagButton(item, item == viewModel.screenState.value.currentHashtag) {
                            viewModel.setCurrentHashtag(
                                item
                            )
                        }
                    }
                }
            }
        if (pinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        text = stringResource(R.string.Pinned),
                        color = AppTheme.colors.textSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(items = pinnedNotes) { item ->
                NoteContent(item, viewModel, currentRoute, navController)
            }
        }
        if (unpinnedNotes.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    stringResource(R.string.Other),
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            items(items = unpinnedNotes) { item ->
                NoteContent(item, viewModel, currentRoute, navController)
            }
        }
    }
}

@Composable
private fun NoteContent(
    item: Note,
    viewModel: CalendarViewModel,
    currentRoute: String,
    navController: NavController
) {
    NoteCard(
        header = item.header,
        text = item.body,
        reminder = rememberNextReminder(
            reminders = viewModel.screenState.value.reminders,
            noteId = item._id,
            pointDate = LocalDateTime.of(
                viewModel.screenState.value.selectedDateRange.first,
                LocalTime.MIN
            )
        ),
        onClick = { viewModel.clickOnNote(item, currentRoute, navController) },
    )
}