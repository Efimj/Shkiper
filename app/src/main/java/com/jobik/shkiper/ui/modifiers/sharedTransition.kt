package com.jobik.shkiper.ui.modifiers

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.jobik.shkiper.ui.components.cards.NoteSharedElementKey
import com.jobik.shkiper.ui.components.cards.NoteSharedElementType
import com.jobik.shkiper.ui.helpers.LocalNavAnimatedVisibilityScope
import com.jobik.shkiper.ui.helpers.LocalSharedElementKey
import com.jobik.shkiper.ui.helpers.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.skipToLookaheadSize() = composed {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

    val sharedTransitionModifier = sharedTransitionScope?.let { scope ->
        animatedVisibilityScope?.let { visibilityScope ->
            with(scope) {
                Modifier
                    .skipToLookaheadSize()
            }
        }
    } ?: Modifier
    this.then(sharedTransitionModifier)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedNoteTransitionModifier(
    noteId: String
) = composed {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
    val sharedElementKey = LocalSharedElementKey.current

    val sharedTransitionModifier = sharedTransitionScope?.let { scope ->
        animatedVisibilityScope?.let { visibilityScope ->
            with(scope) {
                Modifier
                    .skipToLookaheadSize()
                    .sharedBounds(
                        rememberSharedContentState(
                            key = NoteSharedElementKey(
                                noteId = noteId,
                                origin = sharedElementKey,
                                type = NoteSharedElementType.Bounds
                            )
                        ),
                        animatedVisibilityScope = visibilityScope
                    )
            }
        }
    } ?: Modifier
    this.then(sharedTransitionModifier)
}