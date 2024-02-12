package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.theme.CustomTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun NoteListScreenCalendarContent(
    navController: NavController,
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            ScreenCalendarTopBar(viewModel = viewModel, onSlideBack = onSlideBack)
        }
    ) {
        val pinnedNotes =
            remember(viewModel.screenState.value.notes) { viewModel.screenState.value.notes.filter { it.isPinned } }
        val unpinnedNotes =
            remember(viewModel.screenState.value.notes) { viewModel.screenState.value.notes.filterNot { it.isPinned } }

        LazyGridNotes(
            contentPadding = PaddingValues(10.dp, 15.dp, 10.dp, 80.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            if (viewModel.screenState.value.hashtags.isNotEmpty())
                item(span = StaggeredGridItemSpan.FullLine) {
                    LazyRow(
                        modifier = Modifier
                            .wrapContentSize(unbounded = true)
                            .width(LocalConfiguration.current.screenWidthDp.dp),
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
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
                            color = CustomTheme.colors.textSecondary,
                            style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
                items(items = pinnedNotes) { item ->
                    NoteCard(
                        header = item.header,
                        text = item.body,
                        reminder = viewModel.screenState.value.reminders.find { it.noteId == item._id },
                        onClick = { },
                    )
                }
            }
            if (unpinnedNotes.isNotEmpty()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        stringResource(R.string.Other),
                        color = CustomTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
                items(items = unpinnedNotes) { item ->
                    NoteCard(
                        item.header,
                        item.body,
                        reminder = viewModel.screenState.value.reminders.find { it.noteId == item._id },
                        onClick = { },
                    )
                }
            }
        }
    }
}

