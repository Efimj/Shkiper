package com.example.notepadapp.page.NoteListPage

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notepadapp.navigation.UserPage
import com.example.notepadapp.ui.components.cards.CreateNoteCard
import com.example.notepadapp.ui.components.cards.NoteCard
import com.example.notepadapp.ui.components.fields.SearchField
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListPage(navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val staggeredGridCellsMode: StaggeredGridCells = remember {
        if (isPortrait) StaggeredGridCells.Fixed(2) else StaggeredGridCells.Adaptive(200.dp)
    }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val toolbarHeight = 60.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
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
                CreateNoteCard("Create", notesViewModel.selectedNoteCardIndices.value.isEmpty()){
                    notesViewModel.createNewNote()
                }
            }
            items(items = notesViewModel.notes.value, key = {it._id.toHexString()}) {
                NoteCard(
                    it.header,
                    it.body,
                    selected = it._id.toHexString() in notesViewModel.selectedNoteCardIndices.value,
                    onClick = {
                        if (notesViewModel.selectedNoteCardIndices.value.isNotEmpty())
                            notesViewModel.toggleSelectedNoteCard(it._id.toHexString())
                        else
                        {

                            if (currentRoute.substringBefore("/") != UserPage.Note.route.substringBefore("/")){
                                navController.navigate(UserPage.Note.noteId(it._id.toHexString()))
                            }
                        }
                    },
                    onLongClick = {
                        notesViewModel.toggleSelectedNoteCard(it._id.toHexString())
                    })
            }
        }
        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .padding(20.dp, 10.dp, 20.dp, 0.dp)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        ) {
            SearchField(search = notesViewModel.searchText, onValueChange = {
                notesViewModel.searchText = it
            })
        }
    }
}
