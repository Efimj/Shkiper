package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
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
import com.jobik.shkiper.ui.components.cards.PurchaseCard
import com.jobik.shkiper.ui.theme.CustomAppTheme

@Composable
fun PurchaseScreen(purchaseViewModel: PurchaseViewModel = hiltViewModel()) {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 85.dp, bottom = 20.dp)) {
            Text(
                stringResource(R.string.PurchaseScreenTitle),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 8.dp).fillMaxWidth()
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            purchaseViewModel.screenState.value.purchases.map {
                PurchaseCard(product = it) {
                    purchaseViewModel.makePurchase(it, context as Activity)
                }
            }
        }
    }

}