package com.example.notepadapp.page

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.components.cards.NoteCard
import com.example.notepadapp.ui.components.fields.SearchField
import kotlin.math.roundToInt


data class Card(
    var title: String,
    val description: String,
    var isSelected: Boolean = false
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
        Card("Список целей на год", "перечень основных целей, которые нужно достичь в течение года."),
        Card(
            "Список дел на выходные",
            "перечень дел, которые можно выполнить на выходных для отдыха и улучшения настроения."
        ),
        Card("Список интересных фильмов", "перечень фильмов, которые рекомендуются к просмотру в свободное время."),
        Card("План обучения новому навыку", "план обучения и практики нового навыка для его освоения."),
        Card("Список желаемых покупок", "перечень вещей, которые хотелось бы приобрести в ближайшее время."),
        Card("План обновления гардероба", "план покупок и обновления гардероба на сезон."),
        Card("Список любимых рецептов", "перечень любимых рецептов, которые можно приготовить для себя и близких."),
        Card("План обновления дома", "план обновления и ремонта дома на ближайшее время."),
        Card("Список целей на месяц", "перечень целей, которые нужно достичь в течение месяца."),
        Card("Идеи для подарков", "записи о потенциальных подарках для близких и друзей на различные праздники."),
        Card("sss", "wddww")
    )
    val (selectedCardIndices, setSelectedCardIndices) = remember { mutableStateOf(setOf<Int>()) }

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

    var search by remember { mutableStateOf("Value") }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyVerticalStaggeredGrid(
            columns = staggeredGridCellsMode,
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp, 80.dp, 10.dp, 100.dp),
            modifier = Modifier.fillMaxSize(),
        ){
            items(noteCards.size) { index ->
                NoteCard(
                    noteCards[index].title,
                    noteCards[index].description,
                    selected = index in selectedCardIndices,
                    onClick = {
                        onNoteClick(selectedCardIndices, index, setSelectedCardIndices)
                    },
                    onLongClick = {
                        changeSelectedMode(selectedCardIndices, index, setSelectedCardIndices)
                    })
            }
        }
        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .padding(20.dp, 10.dp, 20.dp, 0.dp)
                .background(Color.Transparent)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        ){
            SearchField(search = search, onValueChange = {
                search = it
            })
        }
    }
}

private fun onNoteClick(
    selectedCardIndices: Set<Int>,
    index: Int,
    setSelectedCardIndices: (Set<Int>) -> Unit
) {
    if (selectedCardIndices.isNotEmpty())
        changeSelectedMode(selectedCardIndices, index, setSelectedCardIndices)
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
