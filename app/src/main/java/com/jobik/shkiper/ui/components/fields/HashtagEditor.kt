package com.jobik.shkiper.ui.components.fields

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.*
import com.jobik.shkiper.ui.helpers.*
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HashtagEditor(
    modifier: Modifier,
    enabled: Boolean = true,
    selectedTags: Set<String>,
    allTags: Set<String>,
    onSave: (Set<String>) -> Unit
) {
    val editModeEnabled = rememberSaveable { mutableStateOf(false) }
    val textFieldFocusRequester by remember { mutableStateOf(FocusRequester()) }
    val textFieldValue = rememberSaveable { mutableStateOf(selectedTags.joinToString(" ")) }
    var isFocused by remember { mutableStateOf(false) }
    val selectableTags = allTags - selectedTags

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
        HashtagsPresentation(hashtags = selectedTags) { if (enabled) editModeEnabled.value = true }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(editModeEnabled.value, showBottomSheet.value) {
        if (editModeEnabled.value) {
            showBottomSheet.value = true
        }
        if (editModeEnabled.value.not()) {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            }
        }
    }

    val topInsetsPaddings = topWindowInsetsPadding()
    val bottomInsetsPaddings = bottomWindowInsetsPadding()
    val horizontalInsetsPaddingsModifier = Modifier.horizontalWindowInsetsPadding()

    if (showBottomSheet.value) {
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
            Spacer(modifier = Modifier.height(topInsetsPaddings))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(horizontalInsetsPaddingsModifier)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {


                Text(text = "Select tags", color = CustomTheme.colors.text)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (hashtag in allTags) {
                        HashtagButton(text = hashtag) { }
                    }
                }
            }

            Spacer(modifier = Modifier.height(bottomInsetsPaddings))
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
private fun HashtagsPresentation(
    hashtags: Set<String>,
    setEditMode: () -> Unit
) {
    if (hashtags.isEmpty()) {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (hashtag in hashtags) {
                HashtagButton(text = hashtag) { setEditMode() }
            }
        }
}