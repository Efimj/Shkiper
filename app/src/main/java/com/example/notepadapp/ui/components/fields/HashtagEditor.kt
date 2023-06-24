package com.example.notepadapp.ui.components.fields

import androidx.compose.foundation.layout.*
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.components.buttons.RoundedButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagEditor(hashtags: Set<String>, onSave: () -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Hashtags", style = MaterialTheme.typography.body1)
            RoundedButton(Modifier, text = "Edit", onClick = {})
        }
        Spacer(Modifier.height(4.dp))
        FlowRow {
            for (hashtag in hashtags) {
                HashtagItem(hashtag, {})
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HashtagItem(chip: String, onChipClicked: (String) -> Unit) {
    val isSelected = remember { mutableStateOf(false) }

    Chip(
        modifier = Modifier
            .padding(4.dp),
        onClick = { onChipClicked(chip) }
    ) {
        Text(chip, modifier = Modifier.padding(8.dp))
    }
}