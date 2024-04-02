package com.jobik.shkiper.ui.components.fields

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NewLabel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.HashtagButton
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.launch

private fun Set<String>.toTagsString(): String {
    return this.joinToString(" ")
}

private fun String.toTagsSet(): Set<String> {
    return this.split(" ").filter { it != "" }.toSet()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEditor(
    modifier: Modifier,
    enabled: Boolean = true,
    selectedTags: Set<String>,
    allTags: Set<String>,
    onSave: (Set<String>) -> Unit
) {
    val editModeEnabled = rememberSaveable { mutableStateOf(false) }
    val createdTags = rememberSaveable(selectedTags) { mutableStateOf(emptySet<String>()) }
    val inputString = rememberSaveable(selectedTags) { mutableStateOf("") }
    val newSelectedTags = rememberSaveable(selectedTags) { mutableStateOf(selectedTags.toTagsString()) }
    val isChanged = selectedTags != newSelectedTags.value.toTagsSet()

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

    val topInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
    val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)

    val contentBottomPaddingValue = if (isChanged) 80.dp else 30.dp
    val contentBottomPadding = animateDpAsState(targetValue = contentBottomPaddingValue, label = "contentBottomPadding")

    if (editModeEnabled.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            dragHandle = null,
            containerColor = Color.Transparent,
            contentColor = CustomTheme.colors.text,
            windowInsets = WindowInsets.ime,
            onDismissRequest = {
                if (sheetState.isVisible.not()) {
                    editModeEnabled.value = false
                }
            }) {
            Box {
                Column {
                    Spacer(modifier = Modifier.windowInsetsPadding(topInsets))
                    Surface(
                        shape = BottomSheetDefaults.ExpandedShape,
                        contentColor = CustomTheme.colors.text,
                        color = CustomTheme.colors.mainBackground,
                        tonalElevation = BottomSheetDefaults.Elevation,
                    ) {
                        Column {
                            Header(
                                inputString = inputString,
                                createdTags = createdTags,
                                newSelectedTags = newSelectedTags
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .windowInsetsPadding(bottomInsets)
                            ) {
                                AnimatedVisibility(visible = createdTags.value.isNotEmpty()) {
                                    TagGroup(
                                        modifier = Modifier.padding(horizontal = 20.dp),
                                        header = stringResource(R.string.New),
                                        tags = createdTags.value,
                                        selected = newSelectedTags.value.toTagsSet()
                                    ) {
                                        newSelectedTags.value = it.toTagsString()
                                    }
                                }

                                AnimatedVisibility(visible = selectedTags.isNotEmpty()) {
                                    Column {
                                        TagGroup(
                                            modifier = Modifier.padding(horizontal = 20.dp),
                                            header = stringResource(R.string.Selected),
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
                                        TagGroup(
                                            modifier = Modifier.padding(horizontal = 20.dp),
                                            header = stringResource(R.string.All),
                                            tags = otherTags,
                                            selected = newSelectedTags.value.toTagsSet()
                                        ) {
                                            newSelectedTags.value = it.toTagsString()
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(contentBottomPadding.value))
                            }
                        }
                    }
                }
                BottomBar(
                    modifier = Modifier.windowInsetsPadding(bottomInsets),
                    isVisible = isChanged
                ) {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        editModeEnabled.value = false
                        onSave(newSelectedTags.value.toTagsSet())
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    inputString: MutableState<String>,
    createdTags: MutableState<Set<String>>,
    newSelectedTags: MutableState<String>
) {
    val placeholder = stringResource(id = R.string.HashtagExample)
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
            .padding(bottom = 10.dp)
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
}

@Composable
private fun BoxScope.BottomBar(modifier: Modifier, isVisible: Boolean, onCreateReminderClick: () -> Unit) {
    Box(
        modifier = modifier
            .align(Alignment.BottomCenter)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically { it / 2 } + expandVertically(
                expandFrom = Alignment.Top,
                clip = false
            ) + fadeIn(),
            exit = slideOutVertically { -it / 2 } + shrinkVertically(
                shrinkTowards = Alignment.Top,
                clip = false
            ) + fadeOut(),
        )
        {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 10.dp)
                    .height(50.dp),
                shape = CustomTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomTheme.colors.textOnActive,
                    containerColor = CustomTheme.colors.active
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = onCreateReminderClick
            ) {
                Text(
                    text = stringResource(R.string.SaveChanges),
                    style = androidx.compose.material.MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = CustomTheme.colors.textOnActive,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = CustomTheme.colors.textSecondary
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
                .animateContentSize()
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