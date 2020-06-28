package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class CartMealRequest(
    @SerializedName("productId")val productId:String,
    @SerializedName("quantity")val quantity:Number,
    @SerializedName("type")val type:String
)