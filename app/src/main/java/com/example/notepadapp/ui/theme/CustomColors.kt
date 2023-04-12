package com.example.notepadapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val mainBackground: Color,
    val stroke: Color,
    val text: Color,
    val textSecondary: Color
)
