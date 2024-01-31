package com.jobik.shkiper.ui.components.modals

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import com.jobik.shkiper.ui.components.layouts.BottomSheetLayoutProvider
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    state: SheetState,
    onCancel: () -> Unit = {},
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    dragHandle: @Composable() (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(
        enabled = state.isVisible,
    ) {
        Log.d("sda", "dsa")
        coroutineScope.launch {
            onCancel()
        }
    }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onCancel,
        shape = shape,
        containerColor = CustomTheme.colors.mainBackground,
        contentColor = CustomTheme.colors.text,
        dragHandle = dragHandle,
    ) {
        BottomSheetLayoutProvider {
            content()
        }
    }
}