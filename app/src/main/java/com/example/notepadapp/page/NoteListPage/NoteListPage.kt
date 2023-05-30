package com.example.notepadapp.page.NoteListPage

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.components.cards.CreateNoteCard
import com.example.notepadapp.ui.components.cards.NoteCard
import com.example.notepadapp.ui.components.fields.SearchBar
import com.example.notepadapp.ui.theme.CustomAppTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteListPage(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val staggeredGridCellsMode: StaggeredGridCells = remember {
        if (isPortrait) StaggeredGridCells.Fixed(2) else StaggeredGridCells.Adaptive(200.dp)
    }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val searchBarHeight = 60.dp
    val searchBarHeightPx = with(LocalDensity.current) { searchBarHeight.roundToPx().toFloat() }
    val searchBarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = searchBarOffsetHeightPx.value + delta
                searchBarOffsetHeightPx.value = newOffset.coerceIn(-searchBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val actionBarHeight = 56.dp
    val actionBarHeightPx = with(LocalDensity.current) { actionBarHeight.roundToPx().toFloat() }
    val offsetX = remember { Animatable(-actionBarHeightPx) }

    LaunchedEffect(notesViewModel.selectedNoteCardIndices.value) {
        if (notesViewModel.selectedNoteCardIndices.value.isEmpty()) {
            offsetX.animateTo(
                targetValue = -actionBarHeightPx,
                animationSpec = tween(durationMillis = 200)
            )
        } else {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200)
            )
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyVerticalStaggeredGrid(
            columns = staggeredGridCellsMode,
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(10.dp, 75.dp, 10.dp, 100.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                CreateNoteCard("Create", notesViewModel.selectedNoteCardIndices.value.isEmpty()) {
                    notesViewModel.createNewNote()
                }
            }
            items(items = notesViewModel.notes.value, key = { it._id.toHexString() }) {
                NoteCard(
                    it.header,
                    it.body,
                    selected = it._id.toHexString() in notesViewModel.selectedNoteCardIndices.value,
                    onClick = { onNoteClick(notesViewModel, it, currentRoute, navController) },
                    onLongClick = { notesViewModel.toggleSelectedNoteCard(it._id.toHexString()) })
            }
        }
        Box(
            modifier = Modifier
        ) {
            SearchBar(searchBarHeight, searchBarOffsetHeightPx, notesViewModel)
            ActionBar(actionBarHeight, offsetX, notesViewModel)
        }
    }
}

@Composable
private fun ActionBar(
    actionBarHeight: Dp,
    offsetX: Animatable<Float, AnimationVector1D>,
    notesViewModel: NotesViewModel
) {
    Box(
        modifier = Modifier
            .height(actionBarHeight)
            .offset { IntOffset(x = 0, y = offsetX.value.roundToInt()) },
    ) {
        androidx.compose.material.TopAppBar(
            elevation = 2.dp,
            contentColor = CustomAppTheme.colors.textSecondary,
            backgroundColor = CustomAppTheme.colors.mainBackground,
            title = {
                Text(
                    notesViewModel.selectedNoteCardIndices.value.count().toString(),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                    color = CustomAppTheme.colors.textSecondary,
                    maxLines = 1,
                )
            },
            navigationIcon = {
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { notesViewModel.clearSelectedNote() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Go back",
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
            },
            actions = {
                androidx.compose.material.IconButton(
                    onClick = { },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PushPin,
                        contentDescription = "Attach a note",
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationAdd,
                        contentDescription = "Add to notification",
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Archive,
                        contentDescription = "Add to archive",
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
                IconButton(
                    onClick = { notesViewModel.deleteSelectedNotes() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Add to archive",
                        tint = CustomAppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SearchBar(
    searchBarHeight: Dp,
    searchBarOffsetHeightPx: MutableState<Float>,
    notesViewModel: NotesViewModel
) {
    Box(
        modifier = Modifier
            .height(searchBarHeight)
            .padding(20.dp, 10.dp, 20.dp, 0.dp)
            .offset { IntOffset(x = 0, y = searchBarOffsetHeightPx.value.roundToInt()) },
    ) {
        SearchBar(search = notesViewModel.searchText, onValueChange = {
            notesViewModel.searchText = it
        })
    }
}

private fun onNoteClick(
    notesViewModel: NotesViewModel,
    it: Note,
    currentRoute: String,
    navController: NavController
) {
    if (notesViewModel.selectedNoteCardIndices.value.isNotEmpty())
        notesViewModel.toggleSelectedNoteCard(it._id.toHexString())
    else {
        if (currentRoute.substringBefore("/") != UserPage.Note.route.substringBefore("/")) {
            navController.navigate(UserPage.Note.noteId(it._id.toHexString()))
        }
    }
}

