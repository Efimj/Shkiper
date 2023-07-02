package com.example.notepadapp.ui.theme

import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor as GraphicsColor

val DefaultLightThemeColors = ExtendedColors(
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    stroke = Color(GraphicsColor("#D2D2D2")),
    text = Color(GraphicsColor("#161616")),
    textSecondary = Color(GraphicsColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
    active = Color(GraphicsColor("#433FFF")),
    secondaryBackground = Color(GraphicsColor("#FFFFFF")),
)

val DefaultDarkThemeColors = ExtendedColors(
    mainBackground = Color(GraphicsColor("#0F0F0F")),
    stroke = Color(GraphicsColor("#404040")),
    text = Color(GraphicsColor("#FFFFFF")),
    textSecondary = Color(GraphicsColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
    active = Color(GraphicsColor("#433FFF")),
    secondaryBackground = Color(GraphicsColor("#191919")),
)