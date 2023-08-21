package com.jobik.shkiper.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomAppTheme

sealed class PurchaseCardContent {
    abstract val imageRes: Int
    abstract val isPurchased: Boolean
    abstract val isHighlighted: Boolean
}

data class ProductPurchaseCardContent(
    val product: ProductDetails,
    override val imageRes: Int,
    override val isPurchased: Boolean = false,
    override val isHighlighted: Boolean = false,
) : PurchaseCardContent()

data class TitlePurchaseCardContent(
    val titleRes: Int,
    override val imageRes: Int,
    override val isPurchased: Boolean = false,
    override val isHighlighted: Boolean = false,
) : PurchaseCardContent()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PurchaseCard(purchaseCardContent: PurchaseCardContent, onClick: () -> Unit) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier
            .bounceClick()
            .height(130.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = { multipleEventsCutter.processEvent { onClick() } }),
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = if (purchaseCardContent.isHighlighted) BorderStroke(2.dp, CustomAppTheme.colors.active) else null,
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = purchaseCardContent.imageRes),
                        contentDescription = stringResource(R.string.StatisticsImage),
                        contentScale = ContentScale.FillHeight
                    )
                    val title = when (purchaseCardContent) {
                        is ProductPurchaseCardContent -> purchaseCardContent.product.name
                        is TitlePurchaseCardContent -> stringResource(purchaseCardContent.titleRes)
                    }
                    Box(
                        modifier = Modifier.align(Alignment.BottomStart).height(40.dp)
                            .fillMaxWidth().background(
                                Brush.verticalGradient(
                                    0F to CustomAppTheme.colors.mainBackground.copy(alpha = 0.0F),
                                    .4F to CustomAppTheme.colors.mainBackground.copy(alpha = 0.70F),
                                    1F to CustomAppTheme.colors.mainBackground.copy(alpha = 0.9F)
                                )
                            )
                    ) {
                        Text(
                            text = title,
                            color = CustomAppTheme.colors.text,
                            style = MaterialTheme.typography.body1.copy(fontSize = 16.sp),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier.align(Alignment.BottomCenter).basicMarquee().padding(horizontal = 15.dp)
                                .padding(bottom = 6.dp)
                        )
                    }
                }
            }
        }
        if (purchaseCardContent.isPurchased)
            Box(modifier = Modifier.padding(end = 6.dp, top = 6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(
                        imageVector = Icons.Outlined.TaskAlt,
                        contentDescription = null,
                        tint = CustomAppTheme.colors.active,
                    )
                }
            }
    }
}