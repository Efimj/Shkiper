package com.example.notepadapp.page

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.components.cards.NoteCard
import com.example.notepadapp.ui.components.fields.SearchField

data class Card(
    var title: String,
    val description: String,
    var isSelected: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesPage() {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val staggeredGridCellsMode: StaggeredGridCells =
        if (isPortrait) StaggeredGridCells.Fixed(2) else StaggeredGridCells.Adaptive(200.dp)
    val noteCards = listOf(
        Card(
            "Покупки на неделю",
            "список продуктов, которые нужно купить на следующую неделю для ежедневных приготовлений пищи.",
        ),
        Card(
            "Список задач на сегодня",
            "перечень задач, которые необходимо выполнить в течение дня для достижения целей.",
        ),
        Card("Идеи для отпуска", "записи о потенциальных местах, которые можно посетить в следующем отпуске."),
        Card("Список любимых книг", "перечень книг, которые прочитал и рекомендую другим для чтения."),
        Card(
            "План тренировок",
            "программа тренировок на неделю, которую нужно выполнить для достижения целей в фитнесе.",
        ),
        Card(
            "Планирование бюджета",
            "записи о расходах и доходах, которые нужно учесть при планировании бюджета на месяц.",
        ),
        Card("Идеи для новых проектов", "идеи о новых проектах, которые можно начать в ближайшее время."),
        Card("Список контактов", "перечень контактов, которые могут быть полезны в работе или личной жизни."),
        Card("План поездки", "записи о дате, месте и бюджете планируемой поездки."),
        Card("Список цитат", "перечень любимых цитат, которые вдохновляют и мотивируют на достижение целей."),
    )
    val (selectedCardIndices, setSelectedCardIndices) = remember { mutableStateOf(setOf<Int>()) }
    var search by remember { mutableStateOf("") }


    Box(Modifier.fillMaxSize()) {
        Column (Modifier.padding(horizontal = 20.dp)){
            SearchField(search = search, onValueChange = {
                search = it
            })
            LazyVerticalStaggeredGrid(
                columns = staggeredGridCellsMode,
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(0.dp, 20.dp, 0.dp, 100.dp),
                content = {
                    items(noteCards.size) { index ->
                        NoteCard(
                            noteCards[index].title,
                            noteCards[index].description,
                            selected = index in selectedCardIndices,
                            onClick = {
                                if (selectedCardIndices.isNotEmpty())
                                    changeSelectedMode(selectedCardIndices, index, setSelectedCardIndices)
                            },
                            onLongClick = {
                                changeSelectedMode(selectedCardIndices, index, setSelectedCardIndices)
                            })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun changeSelectedMode(
    selectedCardIndices: Set<Int>,
    index: Int,
    setSelectedCardIndices: (Set<Int>) -> Unit
) {
    if (selectedCardIndices.contains(index))
        setSelectedCardIndices(selectedCardIndices - index)
    else
        setSelectedCardIndices(selectedCardIndices + index)
}
