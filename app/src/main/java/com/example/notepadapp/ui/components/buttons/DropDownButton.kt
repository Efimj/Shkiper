package com.example.notepadapp.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme

enum class DropDownButtonSizeMode {
    STRERCHBYBUTTONWIDTH,
    STRERCHBYCONTENT
}

@Composable
fun DropDownButton(
    items: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    stretchMode: DropDownButtonSizeMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
    onChangedSelection: (newSelectedIndex: Int) -> Unit,
    button: @Composable ((() -> Unit) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        if (button != null)
            button { expanded = !expanded }
        else
            RoundedButton(
                modifier = Modifier.fillMaxWidth(),
                text = items[selectedIndex],
                onClick = { expanded = !expanded },
            )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = (if (stretchMode == DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH)
                Modifier.width(maxWidth) else Modifier)
                .background(CustomAppTheme.colors.mainBackground)
                .clip(RoundedCornerShape(15.dp))
                .border(1.dp, CustomAppTheme.colors.stroke, RoundedCornerShape(15.dp))
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    onChangedSelection(index)
                    expanded = false
                }) {
                    Text(text = s, color = CustomAppTheme.colors.text, style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}