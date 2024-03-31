package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    state: SheetState,
    onCancel: () -> Unit = {},
    makeVerticalScrollable: Boolean = true,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    windowInsets: WindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical),
    dragHandle: @Composable() (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable() (ColumnScope.() -> Unit)
) {
    fun Modifier.addVerticalScroll() = composed {
        if (makeVerticalScrollable)
            this.verticalScroll(rememberScrollState())
        else
            this
    }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onCancel,
        shape = shape,
        containerColor = CustomTheme.colors.mainBackground,
        contentColor = CustomTheme.colors.text,
        dragHandle = dragHandle,
        windowInsets = WindowInsets.ime
    ) {
        Column(
            modifier = Modifier
                .addVerticalScroll()
                .windowInsetsPadding(windowInsets)
                .padding(bottom = 10.dp)
        ) {
            content()
        }
    }
}