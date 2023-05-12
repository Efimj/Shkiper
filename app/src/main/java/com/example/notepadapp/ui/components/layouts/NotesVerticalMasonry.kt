package com.example.notepadapp.ui.components.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notepadapp.page.Card
import com.example.notepadapp.ui.components.cards.NoteCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesVerticalMasonry(
    staggeredGridCellsMode: StaggeredGridCells,
    noteCards: List<Card>,
    selectedCardIndices: Set<Int>,
    state: LazyStaggeredGridState,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp, 10.dp, 0.dp, 100.dp),
    onNoteClick: (Int) -> Unit,
    onNoteLongClick: (Int) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = staggeredGridCellsMode,
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = contentPaddingValues,
        content = {
            items(noteCards.size) { index ->
                NoteCard(
                    noteCards[index].title,
                    noteCards[index].description,
                    selected = index in selectedCardIndices,
                    onClick = {
                        onNoteClick(index)
                    },
                    onLongClick = {
                        onNoteLongClick(index)
                    })
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
