package com.jobik.shkiper.ui.components.layouts

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.ui.Modifier
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.navigation.LocalNavAnimatedVisibilityScope
import com.jobik.shkiper.navigation.LocalSharedTransitionScope
import com.jobik.shkiper.ui.components.cards.NoteCard
import com.jobik.shkiper.ui.helpers.rememberNextReminder
import org.mongodb.kbson.ObjectId

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyStaggeredGridScope.notesList(
    notes: List<Note>,
    reminders: List<Reminder> = emptyList(),
    selected: Set<ObjectId> = emptySet(),
    marker: String? = null,
    onClick: (Note) -> Unit,
    onLongClick: ((Note) -> Unit)? = null,
) {
    items(items = notes, key = { it._id.toHexString() }) { note ->
        val sharedTransitionScope = LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")

        val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

        with(sharedTransitionScope) {
            NoteCard(
                animatedVisibilityScope = animatedVisibilityScope,
                note = note,
                modifier = Modifier.animateItem(),
                reminder = rememberNextReminder(reminders = reminders.filter { it.noteId == note._id }),
                markedText = marker,
                selected = note._id in selected,
                onClick = { onClick(note) },
                onLongClick = {
                    if (onLongClick != null) {
                        onLongClick(note)
                    }
                },
            )
        }
    }
}