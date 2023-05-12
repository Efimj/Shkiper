package com.example.notepadapp.page

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notepadapp.ui.components.fields.SearchField
import com.example.notepadapp.ui.components.layouts.NotesVerticalMasonry

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

    val viewModel = ExampleViewModel()
    val scrollState = rememberLazyStaggeredGridState()
    val scrollUpState = viewModel.scrollUp.observeAsState()

    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    Box(Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
        NotesVerticalMasonry(
            staggeredGridCellsMode,
            noteCards,
            selectedCardIndices,
            scrollState,
            PaddingValues(0.dp, 70.dp, 0.dp, 100.dp),
            { onNoteClick(selectedCardIndices, it, setSelectedCardIndices) }
        ) { changeSelectedMode(selectedCardIndices, it, setSelectedCardIndices) }
        ScrollableAppBar(
            scrollUpState = scrollUpState
        )
//        Box(
//            Modifier.padding(horizontal = 10.dp).background(Color.Transparent).align(Alignment.CenterStart)
//        ) {
//            SearchField(search = search, onValueChange = {
//                search = it
//            })
//        }
    }
}

@Composable
fun ScrollableAppBar(
    scrollUpState: State<Boolean?>,
) {
    val position by animateFloatAsState(if (scrollUpState.value == true) -170f else 0f)
    var search by remember { mutableStateOf("Value") }

    Box(
        Modifier
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Transparent)
            .graphicsLayer { translationY = (position) }
            .clip(RoundedCornerShape(15.dp))
            .padding(horizontal = 10.dp)
            .background(Color.Transparent),
    ) {
        SearchField(search = search, onValueChange = {
            search = it
        })
    }

}

class ExampleViewModel : ViewModel() {

    private var lastScrollIndex = 0

    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
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
