package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetLayoutProvider(content: @Composable() (ColumnScope.() -> Unit)) {
    Column(modifier = Modifier.padding(bottom = 50.dp, top = 10.dp)) {
        content()
    }
}