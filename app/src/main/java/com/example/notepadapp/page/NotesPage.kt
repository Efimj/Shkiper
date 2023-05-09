package com.example.notepadapp.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.ui.theme.CustomAppTheme

@Composable
fun NotesPage() {
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
    {
        Column(Modifier.fillMaxSize()) {
            Text(
                color = CustomAppTheme.colors.text,
                text = "Home",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 65.dp, bottom = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
