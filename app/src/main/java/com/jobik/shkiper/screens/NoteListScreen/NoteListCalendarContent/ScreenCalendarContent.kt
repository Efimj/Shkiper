package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.layouts.CalendarDayView
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.helpers.UpdateStatusBarColor
import com.jobik.shkiper.ui.helpers.displayText
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.SnapConfig
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun ScreenCalendarContent(
    navController: NavController,
    viewModel: CalendarViewModel,
    onSlideBack: () -> Unit,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    AnimateVerticalSwitch(
        modifier = Modifier,
        state = viewModel.screenState.value.fullScreenCalendarOpen.not(),
        topComponent = {
//            UpdateStatusBarColor(
//                current = CustomTheme.colors.mainBackground,
//                previous = CustomTheme.colors.secondaryBackground
//            )

            val today = remember { LocalDate.now() }
            val currentMonth = remember(today) { today.yearMonth }
            val startMonth = remember { currentMonth }
            val endMonth = remember { currentMonth.plusMonths(500) }
            val daysOfWeek = remember { daysOfWeek() }
            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first(),
                outDateStyle = OutDateStyle.EndOfGrid
            )

            BackHandler(viewModel.screenState.value.fullScreenCalendarOpen) {
                viewModel.switchFullScreenCalendarOpen()
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(modifier = Modifier.padding(vertical = 20.dp)) {
                    for (dayOfWeek in daysOfWeek) {
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            color = CustomTheme.colors.textSecondary,
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                VerticalCalendar(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    calendarScrollPaged = false,
                    contentHeightMode = ContentHeightMode.Wrap,
                    contentPadding = PaddingValues(vertical = 20.dp),
                    dayContent = { day ->
                        CalendarDayView(
                            modifier = Modifier.padding(4.dp),
                            day = day,
                            isSelected = viewModel.screenState.value.selectedDateRange.first == day.date || viewModel.screenState.value.selectedDateRange.second == day.date,
                            showIndicator = day.date in viewModel.screenState.value.datesWithIndicator,
                        ) {
                            viewModel.selectDate(date = day.date)
                        }
                    },
                    monthHeader = { month ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                text = month.yearMonth.displayText(),
                                color = CustomTheme.colors.text,
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    monthBody = { _, content ->
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            content()
                        }
                    },
                )
            }
        }) {
        UpdateStatusBarColor(
            current = CustomTheme.colors.secondaryBackground,
            previous = CustomTheme.colors.mainBackground
        )

        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = collapsingToolbarScaffold,
            scrollStrategy = ScrollStrategy.EnterAlways,
            enabledWhenBodyUnfilled = false,
            snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
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
                modifier = Modifier.fillMaxWidth(),
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
                        NoteContent(item, viewModel, currentRoute, navController)
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
                        NoteContent(item, viewModel, currentRoute, navController)
                    }
                }
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