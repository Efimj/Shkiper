package com.android.notepad.ui.theme

import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor as GraphicsColor

val DefaultLightColors = ExtendedColors(
    active = Color(GraphicsColor("#433FFF")),
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    secondaryBackground = Color(GraphicsColor("#FFFFFF")),
    stroke = Color(GraphicsColor("#D2D2D2")),
    text = Color(GraphicsColor("#161616")),
    textSecondary = Color(GraphicsColor("#7A7A7A")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val DefaultDarkColors = ExtendedColors(
    active = Color(GraphicsColor("#433FFF")),
    mainBackground = Color(GraphicsColor("#0F0F0F")),
    secondaryBackground = Color(GraphicsColor("#191919")),
    stroke = Color(GraphicsColor("#404040")),
    text = Color(GraphicsColor("#FFFFFF")),
    textSecondary = Color(GraphicsColor("#959595")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val VueLightColors = ExtendedColors(
    active = Color(GraphicsColor("#42B883")),
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    secondaryBackground = Color(GraphicsColor("#f9f9f9")),
    stroke = Color(GraphicsColor("#f9f9f9")),
    text = Color(GraphicsColor("#213547")),
    textSecondary = Color(60, 60, 60, (255 * 0.7).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val VueDarkColors = ExtendedColors(
    active = Color(GraphicsColor("#42B883")),
    mainBackground = Color(GraphicsColor("#1A1A1A")),
    secondaryBackground = Color(GraphicsColor("#242424")),
    stroke = Color(GraphicsColor("#242424")),
    text = Color(255, 255, 255, (255 * 0.87).toInt()),
    textSecondary = Color(235, 235, 235, (255 * 0.6).toInt()),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val TelegramLightColors = ExtendedColors(
    active = Color(GraphicsColor("#2AABEE")),
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    secondaryBackground = Color(GraphicsColor("#f9f9f9")),
    stroke = Color(GraphicsColor("#f9f9f9")),
    text = Color(GraphicsColor("#000000")),
    textSecondary = Color(GraphicsColor("#878C8E")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val TelegramDarkColors = ExtendedColors(
    active = Color(GraphicsColor("#2AABEE")),
    mainBackground = Color(GraphicsColor("#212121")),
    secondaryBackground = Color(GraphicsColor("#2B2B2B")),
    stroke = Color(GraphicsColor("#2B2B2B")),
    text = Color(GraphicsColor("#FFFFFF")),
    textSecondary = Color(GraphicsColor("#A1A1A1")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val LaravelLightColors = ExtendedColors(
    active = Color(GraphicsColor("#F9322C")),
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    secondaryBackground = Color(GraphicsColor("#F8F8FB")),
    stroke = Color(GraphicsColor("#F8F8FB")),
    text = Color(GraphicsColor("#000000")),
    textSecondary = Color(GraphicsColor("#585656")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val LaravelDarkColors = ExtendedColors(
    active = Color(GraphicsColor("#F9322C")),
    mainBackground = Color(GraphicsColor("#171923")),
    secondaryBackground = Color(GraphicsColor("#14161F")),
    stroke = Color(GraphicsColor("#14161F")),
    text = Color(GraphicsColor("#E7E8EC")),
    textSecondary = Color(GraphicsColor("#B5B5BD")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)

val InstagramLightColors = ExtendedColors(
    active = Color(GraphicsColor("#FF3040")),
    mainBackground = Color(GraphicsColor("#FFFFFF")),
    secondaryBackground = Color(GraphicsColor("#FFFFFF")),
    stroke = Color(GraphicsColor("#DBDBDB")),
    text = Color(GraphicsColor("#000000")),
    textSecondary = Color(GraphicsColor("#737373")),
    modalBackground = Color(0, 0, 0, (255 * 0.2).toInt()),
)

val InstagramDarkColors = ExtendedColors(
    active = Color(GraphicsColor("#FF3040")),
    mainBackground = Color(GraphicsColor("#000000")),
    secondaryBackground = Color(GraphicsColor("#000000")),
    stroke = Color(GraphicsColor("#262626")),
    text = Color(GraphicsColor("#FFFFFF")),
    textSecondary = Color(GraphicsColor("#A8A8A8")),
    modalBackground = Color(0, 0, 0, (255 * 0.4).toInt()),
)
