package com.jobik.shkiper.ui.components.fields

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.*
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.AppTheme

data class SearchBarActionButton(
    val icon: ImageVector,
    @StringRes
    val contentDescription: Int,
    val onClick: () -> Unit
)

@Keep
private const val SearchBarHeight = 64

@Composable
fun getSearchBarHeight() = SearchBarHeight.dp + topWindowInsetsPadding()

@Composable
fun SearchBar(
    isVisible: Boolean,
    value: String,
    actionButton: SearchBarActionButton? = null,
    onChange: (newValue: String) -> Unit
) {
    val isFocused = remember { mutableStateOf(false) }
    val startPaddings by animateDpAsState(
        if (isFocused.value) 0.dp else startWindowInsetsPadding() + 20.dp,
        label = "horizontalPaddings"
    )
    val endPaddings by animateDpAsState(
        if (isFocused.value) 0.dp else endWindowInsetsPadding() + 20.dp,
        label = "horizontalPaddings"
    )
    val topPadding by animateDpAsState(
        if (isFocused.value) 0.dp else topWindowInsetsPadding() + 10.dp,
        label = "topPadding"
    )

    AnimatedVisibility(
        visible = isVisible || isFocused.value,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
    ) {
        Row(
            modifier = Modifier
                .height(getSearchBarHeight())
                .padding(start = startPaddings, end = endPaddings)
                .padding(top = topPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SearchField(
                isFocused = isFocused,
                value = value,
                onChange = onChange,
            )
            ActionButton(actionButton = actionButton, isFocused = isFocused)
        }
    }
}

@Composable
private fun RowScope.ActionButton(
    actionButton: SearchBarActionButton?,
    isFocused: MutableState<Boolean>
) {
    actionButton.let {
        if (it == null) return@let
        AnimatedVisibility(
            modifier = Modifier.heightIn(max = 50.dp),
            visible = !isFocused.value,
            enter = slideInHorizontally() { it / 2 } + expandHorizontally(
                expandFrom = Alignment.Start,
                clip = false
            ) + fadeIn(),
            exit = slideOutHorizontally() { -it / 2 } + shrinkHorizontally(
                shrinkTowards = Alignment.Start,
                clip = false
            ) + fadeOut(),
        )
        {
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.onSecondaryContainer,
                    containerColor = AppTheme.colors.secondaryContainer
                ),
                elevation = null,
                contentPadding = PaddingValues(horizontal = 10.dp),
                onClick = it.onClick
            ) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = stringResource(id = it.contentDescription),
                    tint = AppTheme.colors.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun RowScope.SearchField(
    isFocused: MutableState<Boolean>,
    value: String,
    onChange: (newValue: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    val topPadding by animateDpAsState(
        if (isFocused.value) topWindowInsetsPadding() else 0.dp,
        label = "topPadding"
    )
    val cornerRadius by animateDpAsState(
        if (isFocused.value) 0.dp else 15.dp,
        label = "cornerRadius"
    )
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .weight(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusRequester.requestFocus() }
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = AppTheme.colors.secondaryContainer,
                shape = RoundedCornerShape(cornerRadius)
            )
            .fillMaxHeight()
            .padding(horizontal = 15.dp)
            .padding(top = topPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = !isFocused.value,
            enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
            exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
        )
        {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.Search),
                tint = AppTheme.colors.onSecondaryContainer
            )
        }
        CustomDefaultTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused.value = it.isFocused },
            text = value,
            onTextChange = onChange,
            singleLine = true,
            placeholder = stringResource(R.string.Search),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colors.onSecondaryContainer),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )
        AnimatedVisibility(
            visible = value.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = { onChange("") },
            ) {
                Icon(
                    tint = AppTheme.colors.onSecondaryContainer,
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.Clear)
                )
            }
        }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible == Keyboard.Closed) {
            isFocused.value = false
            focusManager.clearFocus()
        }
    }
}