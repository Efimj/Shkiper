package com.jobik.shkiper.services.billing_service

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

sealed class PurchaseEvent {
    data class PurchaseSuccess(val purchases: List<Purchase>) : PurchaseEvent()
    data class PurchaseFailure(val responseCode: Int) : PurchaseEvent()
}