package com.jobik.shkiper.ui.components.fields

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.Keyboard
import com.jobik.shkiper.ui.helpers.keyboardAsState
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlin.math.roundToInt

@Composable
fun SearchBar(
    searchBarHeight: Dp,
    searchBarOffsetHeightPx: Float,
    isVisible: Boolean,
    value: String,
    onChange: (newValue: String) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val isKeyboardVisible by keyboardAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val searchBarFloatHeight = with(LocalDensity.current) { searchBarHeight.roundToPx().toFloat() }
    val horizontalPaddings by animateDpAsState(if (isFocused) 0.dp else 20.dp, label = "horizontalPaddings")
    val topPadding by animateDpAsState(if (isFocused) 0.dp else 10.dp, label = "topPadding")
    val barHeight by animateDpAsState(if (isFocused) 60.dp else 50.dp, label = "barHeight")
    val cornerRadius by animateDpAsState(if (isFocused) 0.dp else 15.dp, label = "cornerRadius")
    val backgoundColorValue =
        if (isFocused) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground
    val backgroundColorStatusBarColor by animateColorAsState(
        backgoundColorValue,
        label = "backgroundColorStatusBarColor",
    )

    LaunchedEffect(isFocused) {
        systemUiController.setStatusBarColor(
            backgoundColorValue
        )
    }

    AnimatedVisibility(
        isVisible,
        enter = slideIn {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
        exit = slideOut {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
    ) {
        Box(
            modifier = Modifier
                .height(searchBarHeight)
                .padding(horizontal = horizontalPaddings)
                .padding(top = topPadding)
                .offset { IntOffset(x = 0, y = searchBarOffsetHeightPx.roundToInt()) },
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusRequester.requestFocus() }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(
                        color = CustomTheme.colors.secondaryBackground,
                        shape = RoundedCornerShape(cornerRadius)
                    )
                    .height(barHeight)
                    .border(1.dp, backgroundColorStatusBarColor, RoundedCornerShape(cornerRadius))
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !isFocused,
                    enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
                    exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
                )
                {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.Search),
                        tint = CustomTheme.colors.textSecondary
                    )
                }
                CustomDefaultTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    text = value,
                    onTextChange = onChange,
                    singleLine = true,
                    placeholder = stringResource(R.string.Search),
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
                    visible = value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                )
                {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(30.dp),
                        onClick = { onChange("") },
                    ) {
                        Icon(
                            tint = CustomTheme.colors.textSecondary,
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.Clear)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible == Keyboard.Closed) {
            isFocused = false
            focusManager.clearFocus()
        }
    }
}