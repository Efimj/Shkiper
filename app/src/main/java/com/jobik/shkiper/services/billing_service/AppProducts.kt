package com.jobik.shkiper.services.billing_service

import androidx.annotation.Keep

@Keep
object AppProducts {
    //Product IDs
    const val ExampleProduct = "cup_of_tea"

    //Subscriptions IDs
    const val sProduct = "cup_of_tea"

    val ListOfProducts = listOf(
        ExampleProduct
    )
    val ListOfSubscriptions = listOf(
        sProduct
    )
}