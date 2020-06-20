package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class Address(
    @SerializedName("latitude")val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("name")val name:String,
    @SerializedName("fullAddress")val fullAddress:String,
    @SerializedName("isDefault") val isDefault:Boolean,
    @SerializedName("id")val id:String,
    @SerializedName("customerId") val customerId:String
)