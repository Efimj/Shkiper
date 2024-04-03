package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.fields.CustomTextField

@Composable
fun InsertLinkDialog(
    onInsert: (text: String, url: String) -> Unit,
    onGoBack: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    val textFieldFocusRequester = remember { FocusRequester() }
    val urlFieldFocusRequester = remember { FocusRequester() }

    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(CustomTheme.shapes.large)
                .background(CustomTheme.colors.container)
                .padding(vertical = 25.dp, horizontal = 25.dp)
        ) {
            Text(
                text = stringResource(id = R.string.InsertLink),
                color = CustomTheme.colors.textSecondary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                fieldModifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(textFieldFocusRequester),
                text = text,
                onChange = { text = it },
                label = stringResource(id = R.string.Text),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        urlFieldFocusRequester.requestFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                fieldModifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(urlFieldFocusRequester),
                text = url,
                onChange = { url = it },
                label = stringResource(id = R.string.Link),
                placeholder = "https://",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 15.dp, alignment = Alignment.End)
            ) {
                CustomButton(
                    text = stringResource(id = R.string.Cancel),
                    style = ButtonStyle.Text,
                    onClick = onGoBack
                )
                CustomButton(
                    text = stringResource(id = R.string.Confirm),
                    style = if (text.trim().isNotEmpty()) ButtonStyle.Filled else ButtonStyle.Outlined,
                    enabled = text.trim().isNotEmpty(),
                    onClick = { onInsert(text.trim(), url.trim()) }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        textFieldFocusRequester.requestFocus()
    }
}

