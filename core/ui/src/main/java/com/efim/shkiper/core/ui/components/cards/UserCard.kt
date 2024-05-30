package com.jobik.shkiper.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme

data class UserCardLink(
    @DrawableRes
    val image: Int,
    val description: String,
    val onClick: () -> Unit,
)

@Composable
fun UserCard(
    @DrawableRes photo: Int,
    name: String? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    links: List<UserCardLink>? = null
) {
    val modifier = if (onClick != null) Modifier
        .bounceClick()
        .clip(RoundedCornerShape(15.dp))
        .clickable(enabled = true) { onClick() }
        .fillMaxWidth()
        .background(AppTheme.colors.container, RoundedCornerShape(15.dp))
        .padding(8.dp)
    else
        Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.container, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .padding(8.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = photo),
                modifier = Modifier
                    .widthIn(max = 50.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentDescription = stringResource(R.string.DevMailHeader),
                contentScale = ContentScale.Fit
            )
            if (name !== null || description !== null)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    name?.let {
                        Text(
                            text = name,
                            color = AppTheme.colors.text,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                    description?.let {
                        Text(
                            text = it,
                            color = AppTheme.colors.textSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            else if (links !== null)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    links.map {
                        LinkButton(it)
                    }
                }
        }
        if (links !== null && (name !== null || description !== null)) {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                links.map {
                    LinkButton(it)
                }
            }
        }
    }
}

@Composable
private fun LinkButton(link: UserCardLink) {
    IconButton(
        onClick = link.onClick,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .padding(0.dp),
    ) {
        Image(
            painter = painterResource(id = link.image),
            contentDescription = link.description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(2.dp)
        )
    }
}