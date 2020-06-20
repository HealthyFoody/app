package com.healthyfoody.app.models

import com.google.gson.annotations.SerializedName

class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)