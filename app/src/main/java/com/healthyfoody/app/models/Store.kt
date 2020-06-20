package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class Store(
    @SerializedName("description")val description :String,
    @SerializedName("latitude")val latitude : Double,
    @SerializedName("longitude")val longitude:Double
)