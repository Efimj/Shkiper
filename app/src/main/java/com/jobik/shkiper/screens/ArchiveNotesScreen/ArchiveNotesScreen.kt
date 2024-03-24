package com.jobik.shkiper.screens.ArchiveNotesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.components.layouts.CustomTopAppBar
import com.jobik.shkiper.ui.components.layouts.LazyGridNotes
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.layouts.TopAppBarItem
import com.jobik.shkiper.ui.components.modals.CreateReminderDialog
import com.jobik.shkiper.ui.components.modals.ReminderDialogProperties
import com.jobik.shkiper.viewmodels.NotesViewModel
import kotlin.math.roundToInt
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.fields.SearchBarHeight
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import com.jobik.shkiper.ui.theme.CustomTheme
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun ArchiveNotesScreen(navController: NavController, archiveViewModel: NotesViewModel = hiltViewModel()) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val searchBarHeightPx = with(LocalDensity.current) { SearchBarHeight.dp.roundToPx().toFloat() }

    val searchBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val lazyGridNotes = rememberLazyStaggeredGridState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = searchBarOffsetHeightPx.value + delta
                if (lazyGridNotes.canScrollForward) searchBarOffsetHeightPx.value =
                    newOffset.coerceIn(-searchBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val actionBarHeight = 56.dp
    val actionBarHeightPx = with(LocalDensity.current) { actionBarHeight.roundToPx().toFloat() }
    val offsetX = remember { Animatable(-actionBarHeightPx) }

    /**
     * When user select note
     */
    BackHandler(
        enabled = archiveViewModel.screenState.value.selectedNotes.isNotEmpty(), onBack =
        archiveViewModel::clearSelectedNote
    )

    /**
     * LaunchedEffect for cases when the number of selected notes changes.
     */
    LaunchedEffect(archiveViewModel.screenState.value.selectedNotes) {
        if (archiveViewModel.screenState.value.selectedNotes.isEmpty()) {
            offsetX.animateTo(
                targetValue = -actionBarHeightPx, animationSpec = tween(durationMillis = 200)
            )
        } else {
            offsetX.animateTo(
                targetValue = 0f, animationSpec = tween(durationMillis = 200)
            )
        }
    }

    /**
     * LaunchedEffect for cases when it is impossible to scroll the list.
     */
    LaunchedEffect(lazyGridNotes.canScrollForward, lazyGridNotes.canScrollBackward) {
        if (!lazyGridNotes.canScrollForward && !lazyGridNotes.canScrollBackward) {
            searchBarOffsetHeightPx.value = 0f
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        if (archiveViewModel.screenState.value.isNotesInitialized && archiveViewModel.screenState.value.notes.isEmpty())
            ScreenContentIfNoData(title = R.string.ArchiveNotesPageHeader, icon = Icons.Outlined.Inbox)
        else
            ScreenContent(lazyGridNotes, archiveViewModel, currentRoute, navController)
        Box(modifier = Modifier) {
            com.jobik.shkiper.ui.components.fields.SearchBar(
                searchBarOffsetHeightPx = searchBarOffsetHeightPx.value,
                isVisible = archiveViewModel.screenState.value.selectedNotes.isEmpty(),
                value = archiveViewModel.screenState.value.searchText,
                onChange = archiveViewModel::changeSearchText
            )
            ActionBar(actionBarHeight, offsetX, archiveViewModel)
        }
    }
}


@Composable
private fun ScreenContent(
    lazyGridNotes: LazyStaggeredGridState,
    notesViewModel: NotesViewModel,
    currentRoute: String,
    navController: NavController
) {
    LazyGridNotes(
        contentPadding = PaddingValues(10.dp, 70.dp, 10.dp, 80.dp),
        modifier = Modifier.fillMaxSize(),
        gridState = lazyGridNotes
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            LazyRow(
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .width(LocalConfiguration.current.screenWidthDp.dp),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
            ) {
                items(items = notesViewModel.screenState.value.hashtags.toList()) { item ->
                    HashtagButton(item, item == notesViewModel.screenState.value.currentHashtag) {
                        notesViewModel.setCurrentHashtag(
                            item
                        )
                    }
                }
            }
        }
        items(items = notesViewModel.screenState.value.notes) { item ->
            NoteCard(
                header = item.header,
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
    if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
        if (notesViewModel.screenState.value.isCreateReminderDialogShow) {
            CreateReminderDialog(
                reminderDialogProperties = ReminderDialogProperties(),
                onGoBack = notesViewModel::switchReminderDialogShow,
                onDelete = null,
                onSave = notesViewModel::createReminder,
            )
        }
    }
}

@Composable
private fun ActionBar(
    actionBarHeight: Dp, offsetX: Animatable<Float, AnimationVector1D>, notesViewModel: NotesViewModel
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColorValue =
        if (notesViewModel.screenState.value.selectedNotes.isNotEmpty()) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground

    LaunchedEffect(notesViewModel.screenState.value.selectedNotes.isNotEmpty()) {
        systemUiController.setStatusBarColor(backgroundColorValue)
    }

    val topAppBarElevation = if (offsetX.value.roundToInt() < -actionBarHeight.value.roundToInt()) 0.dp else 2.dp
    Box(
        modifier = Modifier
            .height(actionBarHeight)
            .offset { IntOffset(x = 0, y = offsetX.value.roundToInt()) },
    ) {
        CustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            elevation = topAppBarElevation,
            backgroundColor = CustomTheme.colors.secondaryBackground,
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
                    icon = Icons.Outlined.PushPin,
                    iconDescription = R.string.AttachNote,
                    onClick = notesViewModel::pinSelectedNotes
                ),
                TopAppBarItem(
                    isActive = false,
                    icon = Icons.Outlined.NotificationAdd,
                    iconDescription = R.string.AddToNotification,
                    onClick = notesViewModel::switchReminderDialogShow
                ),
                TopAppBarItem(
                    isActive = false,
                    icon = Icons.Outlined.Unarchive,
                    iconDescription = R.string.AddToArchive,
                    onClick = notesViewModel::unarchiveSelectedNotes
                ),
                TopAppBarItem(
                    isActive = false,
                    icon = Icons.Outlined.Delete,
                    iconDescription = R.string.AddToBasket,
                    onClick = notesViewModel::moveSelectedNotesToBasket
                ),
            )
        )
    }
}

