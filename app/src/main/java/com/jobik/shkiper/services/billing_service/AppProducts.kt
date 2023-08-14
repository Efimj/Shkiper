package com.jobik.shkiper.services.billing_service

import androidx.annotation.Keep

@Keep
object AppProducts {
    //Product IDs
    const val CupOfTea = "cup_of_tea"
    const val PepperoniPizza = "pepperoni_pizza"
    const val GymPass = "gym_pass"

    //Subscriptions IDs
    const val Monthly = "monthly_subscription"

    val ListOfProducts = listOf(
        CupOfTea,
        GymPass,
        PepperoniPizza
    )
    val ListOfSubscriptions = listOf(
        Monthly
    )
}