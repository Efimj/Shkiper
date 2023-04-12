package com.example.notepadapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.components.RoundedButton
import com.example.notepadapp.viewmodel.ThemeViewModel

@Composable
fun HomeScreen() {
//    val viewModel = remember { ThemeViewModel() }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//    ) {
//        Text(
//            modifier = Modifier.weight(1f),
//            text = viewModel.isDarkTheme.value.toString(),
//            fontSize = MaterialTheme.typography.h4.fontSize
//        )
//        RoundedButton(
//            modifier = Modifier.width(160.dp),
//            text = "s",
//            onClick = { viewModel.toggleTheme() },
//        )
//    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
