package com.example.notepadapp.page.NoteListPage

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.NoteMongoRepository
import com.example.notepadapp.database.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

//data class NoteCard(
//    var title: String,
//    val description: String,
//    var isSelected: Boolean = false
//)
//
//val NoteCards = listOf(
//    NoteCard(
//        "Покупки на неделю",
//        "список продуктов, которые нужно купить на следующую неделю для ежедневных приготовлений пищи.",
//    ),
//    NoteCard(
//        "Список задач на сегодня",
//        "перечень задач, которые необходимо выполнить в течение дня для достижения целей.",
//    ),
//    NoteCard("Идеи для отпуска", "записи о потенциальных местах, которые можно посетить в следующем отпуске."),
//    NoteCard("Список любимых книг", "перечень книг, которые прочитал и рекомендую другим для чтения."),
//    NoteCard(
//        "План тренировок",
//        "программа тренировок на неделю, которую нужно выполнить для достижения целей в фитнесе.",
//    ),
//    NoteCard(
//        "Планирование бюджета",
//        "записи о расходах и доходах, которые нужно учесть при планировании бюджета на месяц.",
//    ),
//    NoteCard("Идеи для новых проектов", "идеи о новых проектах, которые можно начать в ближайшее время."),
//    NoteCard("Список контактов", "перечень контактов, которые могут быть полезны в работе или личной жизни."),
//    NoteCard("План поездки", "записи о дате, месте и бюджете планируемой поездки."),
//    NoteCard("Список цитат", "перечень любимых цитат, которые вдохновляют и мотивируют на достижение целей."),
//    NoteCard("Список целей на год", "перечень основных целей, которые нужно достичь в течение года."),
//    NoteCard(
//        "Список дел на выходные",
//        "перечень дел, которые можно выполнить на выходных для отдыха и улучшения настроения."
//    ),
//    NoteCard("Список интересных фильмов", "перечень фильмов, которые рекомендуются к просмотру в свободное время."),
//    NoteCard("План обучения новому навыку", "план обучения и практики нового навыка для его освоения."),
//    NoteCard("Список желаемых покупок", "перечень вещей, которые хотелось бы приобрести в ближайшее время."),
//    NoteCard("План обновления гардероба", "план покупок и обновления гардероба на сезон."),
//    NoteCard("Список любимых рецептов", "перечень любимых рецептов, которые можно приготовить для себя и близких."),
//    NoteCard("План обновления дома", "план обновления и ремонта дома на ближайшее время."),
//    NoteCard("Список целей на месяц", "перечень целей, которые нужно достичь в течение месяца."),
//    NoteCard("Идеи для подарков", "записи о потенциальных подарках для близких и друзей на различные праздники."),
//    NoteCard("sss", "wddww")
//)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteMongoRepository
) : ViewModel() {
    val notes = mutableStateOf(emptyList<Note>())

    init {
        viewModelScope.launch {
            repository.getNotes().collect() {
                notes.value = it
            }
        }
    }

    private val _selectedNoteCardIndices = mutableStateOf(setOf<String>())
    val selectedNoteCardIndices: State<Set<String>> = _selectedNoteCardIndices

    fun clearSelectedNote() {
        _selectedNoteCardIndices.value = setOf()
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            val listId = mutableListOf<ObjectId>()
            for (id in selectedNoteCardIndices.value) {
                listId += ObjectId(id)
            }
            repository.deleteNote(listId)
            clearSelectedNote()
        }
    }

    fun toggleSelectedNoteCard(index: String) {
        _selectedNoteCardIndices.value = if (_selectedNoteCardIndices.value.contains(index))
            _selectedNoteCardIndices.value.minus(index)
        else
            _selectedNoteCardIndices.value.plus(index)
    }

    fun createNewNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(Note())
        }
    }

    var searchText by mutableStateOf("")
}