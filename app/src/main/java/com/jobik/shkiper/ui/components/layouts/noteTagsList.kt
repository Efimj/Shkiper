package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.components.buttons.HashtagButton

fun LazyStaggeredGridScope.noteTagsList(
    tags: Set<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    if (tags.isNotEmpty()) {
        item(span = StaggeredGridItemSpan.FullLine) {
            LazyRow(
                modifier = Modifier
                    .animateItem()
                    .wrapContentSize(unbounded = true)
                    .width(LocalConfiguration.current.screenWidthDp.dp),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = tags.toList(), key = { it }) { tag ->
                    HashtagButton(
                        modifier = Modifier.animateItem(),
                        text = tag,
                        selected = tag == selected,
                        onClick = onSelect
                    )
                }
            }
        }
    }
}
