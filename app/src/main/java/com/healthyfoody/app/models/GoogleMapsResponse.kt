package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class GoogleMapsResponse (
    @SerializedName("results")
    val response: List<GoogleMapsResultBody>
)