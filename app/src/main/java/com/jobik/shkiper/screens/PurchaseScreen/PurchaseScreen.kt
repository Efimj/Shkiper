package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DataExploration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jobik.shkiper.R
import com.jobik.shkiper.services.billing_service.AppProducts
import com.jobik.shkiper.ui.components.cards.PurchaseCard
import com.jobik.shkiper.ui.components.cards.PurchaseCardContent
import com.jobik.shkiper.ui.theme.CustomAppTheme

@Composable
fun PurchaseScreen(purchaseViewModel: PurchaseViewModel = hiltViewModel()) {
    val context = LocalContext.current

    ColumnForItems() {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 85.dp, bottom = 20.dp)) {
            Text(
                stringResource(R.string.PurchaseScreenTitle),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 12.dp).fillMaxWidth()
            )
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DataExploration,
                    contentDescription = stringResource(R.string.Info),
                    tint = CustomAppTheme.colors.textSecondary
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.PurchaseScreenDescription),
                    color = CustomAppTheme.colors.text,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Text(
            stringResource(R.string.BuyMe),
            color = CustomAppTheme.colors.text,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 4.dp).fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val productList = listOf(
                purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.CupOfTea }?.let {
                    PurchaseCardContent(
                        product = it,
                        image = R.drawable.ic_notification
                    )
                },
                purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.PepperoniPizza }?.let {
                    PurchaseCardContent(
                        product = it,
                        image = R.drawable.ic_notification,
                        isBestOffer = true
                    )
                },
                purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.GymPass }?.let {
                    PurchaseCardContent(
                        product = it,
                        image = R.drawable.ic_notification
                    )
                }
            )
            for (product in productList)
                if (product != null)
                    Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                        PurchaseCard(product) {
                            purchaseViewModel.makePurchase(product.product, context as Activity)
                        }
                    }
        }
        Text(
            stringResource(R.string.BuySubscription),
            color = CustomAppTheme.colors.text,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 4.dp).padding(top = 8.dp).fillMaxWidth()
        )
    }
}

@Composable
private fun ColumnForItems(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 550.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}