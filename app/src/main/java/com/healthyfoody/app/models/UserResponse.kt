package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String)