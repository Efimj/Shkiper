package com.android.notepad.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Redo
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.notepad.R
import com.android.notepad.ui.theme.CustomAppTheme

enum class DropDownButtonSizeMode {
    STRERCHBYBUTTONWIDTH,
    STRERCHBYCONTENT
}

data class DropDownItem(
    val text: String,
    val icon: ImageVector? = null,
)

@Composable
fun DropDownButton(
    items: List<DropDownItem>,
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
                text = items[selectedIndex].text,
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
            items.forEachIndexed { index, dropDownItem ->
                if (dropDownItem.icon == null)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dropDownItem.text,
                                color = CustomAppTheme.colors.text,
                                style = MaterialTheme.typography.body1
                            )
                        },
                        onClick = {
                            onChangedSelection(index)
                            expanded = false
                        })
                else
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dropDownItem.text,
                                color = CustomAppTheme.colors.text,
                                style = MaterialTheme.typography.body1
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = dropDownItem.icon,
                                contentDescription = stringResource(R.string.GoForward),
                                tint = CustomAppTheme.colors.textSecondary,
                            )
                        },
                        onClick = {
                            onChangedSelection(index)
                            expanded = false
                        })
            }
        }
    }
}