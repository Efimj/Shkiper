package com.jobik.shkiper.ui.helpers

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import com.jobik.shkiper.database.models.NotePosition

val LocalNotePosition = compositionLocalOf { NotePosition.MAIN }
val LocalSharedElementKey = compositionLocalOf { "default" }
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }