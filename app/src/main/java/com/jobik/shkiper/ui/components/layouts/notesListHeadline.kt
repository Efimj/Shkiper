package com.jobik.shkiper.ui.components.layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

fun LazyStaggeredGridScope.notesListHeadline(@StringRes headline: Int) {
    item(span = StaggeredGridItemSpan.FullLine) {
        Text(
            text = stringResource(id = headline),
            color = AppTheme.colors.textSecondary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.animateItem().padding(horizontal = 10.dp)
        )
    }
}