package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class UserRequest(
    @SerializedName("email")
    val email :String,
    @SerializedName("password")
    val password: String,
    @SerializedName("firstName")
    val firstName : String,
    @SerializedName("lastName")
    val lastName :String)