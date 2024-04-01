package com.jobik.shkiper.ui.components.fields

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.NewLabel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.screens.NoteScreen.NoteViewModel
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.launch

private fun Set<String>.toTagsString(): String {
    return this.joinToString(" ")
}

private fun String.toTagsSet(): Set<String> {
    return this.split(' ').toSet()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TagEditor(
    modifier: Modifier,
    enabled: Boolean = true,
    selectedTags: Set<String>,
    allTags: Set<String>,
    onSave: (Set<String>) -> Unit
) {
    val editModeEnabled = rememberSaveable { mutableStateOf(false) }
    val createdTags = rememberSaveable { mutableStateOf(emptySet<String>()) }
    val inputString = rememberSaveable { mutableStateOf("") }
    val newSelectedTags = rememberSaveable { mutableStateOf(selectedTags.toTagsString()) }

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
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                color = CustomTheme.colors.textSecondary
            )
        }
        TagsPresentation(tags = selectedTags) { if (enabled) editModeEnabled.value = true }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val verticalInsets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical)

    if (editModeEnabled.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            dragHandle = null,
            containerColor = CustomTheme.colors.mainBackground,
            contentColor = CustomTheme.colors.text,
            windowInsets = WindowInsets.ime,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (sheetState.isVisible.not()) {
                        editModeEnabled.value = false
                    }
                }
            }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(verticalInsets)
            ) {

                val placeholder = stringResource(id = R.string.HashtagExample)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .height(IntrinsicSize.Min)
                ) {
                    CustomOutlinedTextField(
                        rowModifier = Modifier.weight(1f),
                        placeholder = placeholder,
                        text = inputString.value,
                        onChange = { inputString.value = it })
                    AnimatedVisibility(
                        visible = inputString.value.isNotBlank(),
                        enter = slideInHorizontally { it } + expandHorizontally(
                            expandFrom = Alignment.Start,
                            clip = false
                        ) + fadeIn(),
                        exit = slideOutHorizontally { it } + shrinkHorizontally(
                            shrinkTowards = Alignment.Start,
                            clip = false
                        ) + fadeOut(),
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                modifier = Modifier.fillMaxHeight(),
                                shape = CustomTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = CustomTheme.colors.text,
                                    containerColor = CustomTheme.colors.secondaryBackground
                                ),
                                border = null,
                                elevation = null,
                                contentPadding = PaddingValues(horizontal = 15.dp),
                                onClick = {
                                    createdTags.value += inputString.value.toTagsSet()
                                    newSelectedTags.value += ' ' + inputString.value
                                    inputString.value = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.NewLabel,
                                    contentDescription = stringResource(R.string.Add),
                                    tint = CustomTheme.colors.text
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = createdTags.value.isNotEmpty()) {
                    TagGroup(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        header = "New tags",
                        tags = createdTags.value,
                        selected = newSelectedTags.value.toTagsSet()
                    ) {
                        newSelectedTags.value = it.toTagsString()
                    }
                }

                AnimatedVisibility(visible = selectedTags.isNotEmpty()) {
                    Column {
                        Divider(
                            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp),
                            color = CustomTheme.colors.secondaryStroke
                        )
                        TagGroup(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            header = "Selected",
                            tags = selectedTags,
                            selected = newSelectedTags.value.toTagsSet()
                        ) {
                            newSelectedTags.value = it.toTagsString()
                        }
                    }
                }

                val otherTags = allTags - selectedTags
                AnimatedVisibility(visible = otherTags.isNotEmpty()) {
                    Column {
                        Divider(
                            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp),
                            color = CustomTheme.colors.secondaryStroke
                        )
                        TagGroup(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            header = "All tags",
                            tags = otherTags,
                            selected = newSelectedTags.value.toTagsSet()
                        ) {
                            newSelectedTags.value = it.toTagsString()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagGroup(
    modifier: Modifier = Modifier,
    header: String,
    tags: Set<String>,
    selected: Set<String>,
    onChange: (Set<String>) -> Unit
) {
    Column(modifier = modifier.animateContentSize()) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Text(
                text = header,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = CustomTheme.colors.text
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (tag in tags) {
                val isSelected = tag in selected
                HashtagButton(text = tag, selected = isSelected) {
                    if (isSelected)
                        onChange(selected - tag)
                    else
                        onChange(selected + tag)
                }
            }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsPresentation(
    tags: Set<String>,
    setEditMode: () -> Unit
) {
    if (tags.isEmpty()) {
        Text(
            stringResource(R.string.HashtagExample),
            style = MaterialTheme.typography.bodyMedium,
            color = CustomTheme.colors.textSecondary
        )
    } else
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { setEditMode() },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (tag in tags) {
                HashtagButton(text = tag) { setEditMode() }
            }
        }
}