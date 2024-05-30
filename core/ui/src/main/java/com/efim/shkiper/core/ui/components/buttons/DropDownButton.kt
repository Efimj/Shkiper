package com.jobik.shkiper.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.ui.theme.AppTheme

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
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    stretchMode: DropDownButtonSizeMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
    onChangedSelection: (newSelectedIndex: Int) -> Unit,
    button: @Composable ((() -> Unit) -> Unit)? = null,
) {
    BoxWithConstraints(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        if (button != null)
            button { expanded.value = !expanded.value }
        else
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = items[selectedIndex].text,
                onClick = { expanded.value = !expanded.value },
            )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = (if (stretchMode == DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH)
                Modifier.width(maxWidth) else Modifier)
                .heightIn(max = 280.dp)
                .background(AppTheme.colors.container)
                .clip(RoundedCornerShape(15.dp))
                .border(1.dp, AppTheme.colors.border, RoundedCornerShape(15.dp))
        ) {
            items.forEachIndexed { index, dropDownItem ->
                if (dropDownItem.icon == null)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dropDownItem.text,
                                color = AppTheme.colors.text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onChangedSelection(index)
                            expanded.value = false
                        })
                else
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dropDownItem.text,
                                color = AppTheme.colors.text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = dropDownItem.icon,
                                contentDescription = stringResource(R.string.GoForward),
                                tint = AppTheme.colors.textSecondary,
                            )
                        },
                        onClick = {
                            onChangedSelection(index)
                            expanded.value = false
                        })
            }
        }
    }
}