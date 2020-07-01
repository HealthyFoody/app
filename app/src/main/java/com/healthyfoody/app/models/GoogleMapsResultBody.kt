package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class GoogleMapsResultBody (
    @SerializedName("formatted_address")
    val formatter_address : String
)