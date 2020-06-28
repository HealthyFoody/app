package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class CartMealItem(
    @SerializedName("id")val id:String,
    @SerializedName("quantity")val quantity: Number,
    @SerializedName("imageUrl")val imageUrl: String,
    @SerializedName("price")val price: Float,
    @SerializedName("description")val description:String,
    @SerializedName("name")val name:String
)