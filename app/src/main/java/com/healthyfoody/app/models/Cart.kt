package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class Cart(
    @SerializedName("id")val id :String,
    @SerializedName("customerId")val customerId : String,
    @SerializedName("meals")val meals: List<CartMealItem>,
    @SerializedName("totalPrice")val totalPrice: Float
)