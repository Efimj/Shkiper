package com.example.notepadapp.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.ui.theme.CustomAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    header: String? = null,
    text: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(max = 250.dp, min = 50.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, if (selected) CustomAppTheme.colors.active else CustomAppTheme.colors.stroke),
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (!header.isNullOrEmpty()) {
                Text(
                    text = header,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp
                )
            }
            if (!text.isNullOrEmpty() && !header.isNullOrEmpty())
                Spacer(modifier = Modifier.height(8.dp))
            if (!text.isNullOrEmpty()) {
                Text(
                    text = text,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.textSecondary,
                )
            }
            if(header.isNullOrEmpty() && text.isNullOrEmpty()){
                Text(
                    text = "Empty note",
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.textSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
