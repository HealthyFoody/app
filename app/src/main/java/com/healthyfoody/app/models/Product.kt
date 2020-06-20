package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("imageUrl")
    val imageUrl : String,
    @SerializedName("price")
    var price : String
)