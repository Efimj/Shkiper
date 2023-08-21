package com.jobik.shkiper.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomAppTheme

data class UserCardLink(
    @DrawableRes
    val image: Int,
    val description: String,
    val onClick: () -> Unit,
)

@Composable
fun UserCard(
    @DrawableRes photo: Int,
    name: String,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    links: List<UserCardLink>? = null
) {
    val modifier = if (onClick != null) Modifier.bounceClick().fillMaxWidth()
        .background(CustomAppTheme.colors.secondaryBackground, RoundedCornerShape(15.dp))
        .border(BorderStroke(width = 1.dp, color = CustomAppTheme.colors.stroke), RoundedCornerShape(15.dp))
        .clip(RoundedCornerShape(15.dp)).padding(8.dp).clickable(enabled = onClick != null) {
            onClick()
        } else Modifier.fillMaxWidth()
        .background(CustomAppTheme.colors.secondaryBackground, RoundedCornerShape(15.dp))
        .border(BorderStroke(width = 1.dp, color = CustomAppTheme.colors.stroke), RoundedCornerShape(15.dp))
        .clip(RoundedCornerShape(15.dp)).padding(8.dp).clickable(enabled = false) {
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = photo),
                modifier = Modifier.widthIn(max = 60.dp).clip(RoundedCornerShape(15.dp)),
                contentDescription = stringResource(R.string.DevMailHeader),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = name,
                    color = CustomAppTheme.colors.text,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                description?.let {
                    Text(
                        text = it,
                        color = CustomAppTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }

            }
        }
        if (links != null)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                links.map {
                    LinkButton(it)
                }
            }
    }
}

@Composable
private fun LinkButton(link: UserCardLink) {
    IconButton(
        onClick = link.onClick,
        modifier = Modifier.size(40.dp).clip(CircleShape).padding(0.dp),
    ) {
        Image(
            painter = painterResource(id = link.image),
            contentDescription = link.description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(2.dp)
        )
    }
}