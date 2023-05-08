package com.example.notepadapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.ui.theme.dark_text
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
            textColor = dark_text,
            onClick = onClick,
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
