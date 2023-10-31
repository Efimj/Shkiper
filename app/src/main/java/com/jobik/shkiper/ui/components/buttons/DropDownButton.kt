package com.jobik.shkiper.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.theme.CustomTheme

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
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = items[selectedIndex].text,
                onClick = { expanded = !expanded },
            )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = (if (stretchMode == DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH)
                Modifier.width(maxWidth) else Modifier)
                .background(CustomTheme.colors.mainBackground)
                .clip(RoundedCornerShape(15.dp))
                .border(1.dp, CustomTheme.colors.stroke, RoundedCornerShape(15.dp))

        ) {
            items.forEachIndexed { index, dropDownItem ->
                if (dropDownItem.icon == null)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dropDownItem.text,
                                color = CustomTheme.colors.text,
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
                                color = CustomTheme.colors.text,
                                style = MaterialTheme.typography.body1
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = dropDownItem.icon,
                                contentDescription = stringResource(R.string.GoForward),
                                tint = CustomTheme.colors.textSecondary,
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