package com.example.notepadapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class CustomColors(
    text: Color,
    textSecondary: Color,
    mainBackground: Color,
    stroke: Color,
    onPrimary: Color? = null,
    isLight: Boolean? = null,
) {
    var text by mutableStateOf(text)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var mainBackground by mutableStateOf(mainBackground)
        private set
    var stroke by mutableStateOf(stroke)
        private set

    fun update(other: CustomColors) {
        text = other.text
        textSecondary = other.textSecondary
        mainBackground = other.mainBackground
        stroke = other.stroke
        // rest of the code here
    }
}
