package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchaseHistoryRecord
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.services.billing_service.BillingService
import com.jobik.shkiper.services.billing_service.PurchaseEvent
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PurchaseScreenState(
    val purchases: List<ProductDetails> = emptyList(),
    val subscriptions: List<ProductDetails> = emptyList(),
    val productsPurchasesHistory: List<PurchaseHistoryRecord> = emptyList(),
    val subscriptionsPurchasesHistory: List<PurchaseHistoryRecord> = emptyList(),
    val isPurchaseComplete: Boolean = false,
)

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {
    private val _screenState = mutableStateOf(PurchaseScreenState())
    val screenState: State<PurchaseScreenState> = _screenState
    private val billingClient = (application as NotepadApplication).billingClientLifecycle

    val purchaseEvents: LiveData<PurchaseEvent>
        get() = billingClient.purchaseEvents

    init {
        _screenState.value = _screenState.value.copy(
            purchases = billingClient.productDetails.value,
            subscriptions = billingClient.subscriptionsDetails.value,
            productsPurchasesHistory = billingClient.productsPurchasesHistory.value,
            subscriptionsPurchasesHistory = billingClient.subscriptionsPurchasesHistory.value
        )
    }

    fun updatePurchasesHistory() {
        _screenState.value = _screenState.value.copy(
            productsPurchasesHistory = billingClient.productsPurchasesHistory.value,
            subscriptionsPurchasesHistory = billingClient.subscriptionsPurchasesHistory.value
        )
    }

    fun makePurchase(productDetails: ProductDetails, activity: Activity) {
        val billingResult = billingClient.makePurchase(activity, productDetails)
        if (billingResult.responseCode == BillingResponseCode.OK) {
            updatePurchasesHistory()
        }
    }

    fun checkIsProductPurchased(productId: String): Boolean {
        return screenState.value.productsPurchasesHistory.any { historyRecord ->
            historyRecord.products.any {
                productId == it
            }
        } || screenState.value.subscriptionsPurchasesHistory.any { historyRecord ->
            historyRecord.products.any {
                productId == it
            }
        }
    }

    fun showCompletedPurchase() {
        _screenState.value = _screenState.value.copy(
            isPurchaseComplete = true,
        )
    }

    fun showSnackbarFailurePurchase(responseCode: Int) {
        viewModelScope.launch {
            when (responseCode) {
                BillingResponseCode.USER_CANCELED -> {

                }

                BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    showSnackbar(
                        message = application.applicationContext.getString(R.string.ItemAlreadyOwned),
                        icon = Icons.Default.Info
                    )
                }

                BillingResponseCode.DEVELOPER_ERROR -> {
                    showSnackbar(
                        message = application.applicationContext.getString(R.string.UnspecifiedErrorOccurred),
                        icon = Icons.Default.Info
                    )
                }
            }
        }
    }

    private suspend fun showSnackbar(message: String, icon: ImageVector?) {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = message,
                icon = icon
            )
        )
    }
}