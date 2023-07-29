package com.android.notepad.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.ui.helpers.MultipleEventsCutter
import com.android.notepad.ui.helpers.get
import com.android.notepad.ui.theme.CustomAppTheme

@Composable
fun MainMenuButton(text: String, icon: ImageVector? = null, isActive: Boolean = false, onClick: () -> Unit = { }){
    val menuButtonModifier = Modifier.fillMaxWidth().height(45.dp)

    if(isActive){
        RoundedButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            border = BorderStroke(0.dp, CustomAppTheme.colors.mainBackground),
            colors = ButtonDefaults.buttonColors(backgroundColor = CustomAppTheme.colors.active),
            textColor = Color.White,
            textStyle = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
            onClick = onClick,
            iconTint = Color.White
        )
    }else{
        RoundedButton(
            text = text,
            icon = icon,
            modifier = menuButtonModifier,
            border = BorderStroke(0.dp, CustomAppTheme.colors.mainBackground),
            textStyle = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
            onClick = onClick,
        )
    }
}
