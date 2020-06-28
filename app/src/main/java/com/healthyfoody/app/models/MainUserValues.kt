package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class MainUserValues {
    @SerializedName("token")
    var token: String ?= null
    @SerializedName("userId")
    var userId: String ?=null
    @SerializedName("sub")
    var email: String ?=null
    @SerializedName("customerId")
    var customerId:String ?=null
    @SerializedName("cartId")
    var cartId:String?=null
}
