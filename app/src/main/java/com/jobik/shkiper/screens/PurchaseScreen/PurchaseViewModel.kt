package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.jobik.shkiper.NotepadApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PurchaseScreenState(
    val purchases: List<ProductDetails> = emptyList()
)

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {
    private val _screenState = mutableStateOf(PurchaseScreenState())
    val screenState: State<PurchaseScreenState> = _screenState

    init {
        val billingClient = (application as NotepadApplication).billingClientLifecycle
        _screenState.value = _screenState.value.copy(purchases = billingClient.productDetails.value)
    }

    fun makePurchase(productDetails: ProductDetails, activity: Activity){
        (application as NotepadApplication).billingClientLifecycle.makePurchase(activity, productDetails)
    }
}