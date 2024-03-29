package com.jobik.shkiper.ui.components.fields

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.*
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun HashtagEditor(
    modifier: Modifier,
    enabled: Boolean = true,
    tags: Set<String>,
    forSelectionTags: Set<String>,
    onSave: (Set<String>) -> Unit
) {
    val editModeEnabled = rememberSaveable { mutableStateOf(false) }
    val textFieldFocusRequester by remember { mutableStateOf(FocusRequester()) }
    val textFieldValue = rememberSaveable { mutableStateOf(tags.joinToString(" ")) }
    var isFocused by remember { mutableStateOf(false) }
    val selectableTags = forSelectionTags - tags

    Column(modifier = modifier.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) { if (enabled) editModeEnabled.value = true })
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.Hashtags),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                color = CustomTheme.colors.textSecondary
            )
        }
        Column(Modifier.fillMaxWidth()) {
            if (editModeEnabled.value) {
                HashtagsEdit(textFieldValue,
                    Modifier
                        .fillMaxWidth()
                        .focusRequester(textFieldFocusRequester)
                        .onFocusChanged { isFocused = it.isFocused }) {
                    saveHandle(onSave, textFieldValue)
                    editModeEnabled.value = !editModeEnabled.value
                }
            } else
                HashtagsPresentation(tags) { if (enabled) editModeEnabled.value = true }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (editModeEnabled.value) {
                Row {
                    CustomButton(
                        Modifier,
                        properties = DefaultButtonProperties(
                            buttonColors = androidx.compose.material.ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                disabledBackgroundColor = Color.Transparent
                            ),
                            horizontalPaddings = 20.dp
                        ),
                        text = stringResource(R.string.Cancel),
                        onClick = { editModeEnabled.value = false },
                        style = ButtonStyle.Text
                    )
                    Spacer(Modifier.width(10.dp))
                    CustomButton(
                        Modifier,
                        properties = DefaultButtonProperties(
                            horizontalPaddings = 20.dp
                        ),
                        text = stringResource(R.string.Save),
                        onClick = {
                            if (editModeEnabled.value)
                                saveHandle(onSave, textFieldValue)
                            editModeEnabled.value = !editModeEnabled.value
                        })
                }
            } else
                if (enabled && selectableTags.isNotEmpty()) {
                    val items = selectableTags.toList()
                    val isExpanded = remember { mutableStateOf(false) }
                    DropDownButton(
                        items = items.map {
                            DropDownItem(text = it)
                        },
                        expanded = isExpanded,
                        selectedIndex = 0,
                        stretchMode = DropDownButtonSizeMode.STRERCHBYCONTENT,
                        onChangedSelection = {
                            val newTagString = textFieldValue.value + " " + items[it]
                            onSave(handleTagListString(newTagString))
                            textFieldValue.value = newTagString
                        }
                    ) {
                        CustomButton(
                            Modifier,
                            properties = DefaultButtonProperties(
                                horizontalPaddings = 20.dp
                            ),
                            text = stringResource(R.string.Add),
                            icon = Icons.Outlined.Bookmarks,
                            onClick = { it() })
                    }
                }
        }
    }

    LaunchedEffect(editModeEnabled.value) {
        if (editModeEnabled.value) {
            textFieldValue.value = tags.joinToString(" ")
            textFieldFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(isFocused) {
        if (!isFocused) {
            editModeEnabled.value = false
        }
    }
}

private fun saveHandle(
    onSave: (Set<String>) -> Unit,
    textFieldValue: MutableState<String>
) {
    onSave(handleTagListString(textFieldValue.value))
}

private fun handleTagListString(
    text: String
): Set<String> {
    return text.split(" ").filter { it != "" }.toSet()
}

@Composable
private fun HashtagsEdit(
    text: MutableState<String>,
    modifier: Modifier,
    onDone: () -> Unit
) {
    CustomDefaultTextField(
        text = text.value,
        onTextChange = { text.value = it },
        placeholder = stringResource(R.string.Text),
        textStyle = MaterialTheme.typography.body1,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HashtagsPresentation(
    hashtags: Set<String>,
    setEditMode: () -> Unit
) {
    if (hashtags.isEmpty()) {
        Text(
            stringResource(R.string.HashtagExample),
            style = MaterialTheme.typography.body1,
            color = CustomTheme.colors.textSecondary
        )
    } else
        FlowRow(Modifier.clickable { setEditMode() }) {
            for (hashtag in hashtags) {
                HashtagItem(hashtag) { setEditMode() }
            }
        }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun HashtagItem(chip: String, onChipClicked: (String) -> Unit) {
    Chip(
        modifier = Modifier
            .padding(end = 8.dp),
        onClick = { onChipClicked(chip) },
        shape = RoundedCornerShape(10.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = CustomTheme.colors.secondaryBackground,
            contentColor = CustomTheme.colors.text
        ),
    ) {
        Text(
            chip,
            modifier = Modifier
                .basicMarquee()
                .padding(8.dp),
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = CustomTheme.colors.text
        )
    }
}