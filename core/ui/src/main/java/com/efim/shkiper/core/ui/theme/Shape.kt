package com.jobik.shkiper.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

val CustomShapes = CustomThemeShapes(
    none = RectangleShape,
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(20.dp),
)

val MaterialShapes = Shapes(
    extraSmall = RoundedCornerShape(15.dp),
    small = RoundedCornerShape(15.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(15.dp),
    extraLarge = RoundedCornerShape(15.dp)
)