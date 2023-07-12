package com.android.notepad.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.android.notepad.ui.theme.CustomAppTheme

@Composable
fun MainMenuButton(text: String, icon: ImageVector? = null, isActive: Boolean = false, onClick: () -> Unit = { }){
    val menuButtonModifier = Modifier.fillMaxWidth().height(50.dp)

    if(isActive){
        RoundedButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            border = BorderStroke(0.dp, CustomAppTheme.colors.mainBackground),
            colors = ButtonDefaults.buttonColors(backgroundColor = CustomAppTheme.colors.active),
            textColor = Color.White,
            onClick = onClick,
            iconTint = Color.White
        )
    }else{
        RoundedButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            border = BorderStroke(0.dp, CustomAppTheme.colors.mainBackground),
            onClick = onClick,
        )
    }
}
