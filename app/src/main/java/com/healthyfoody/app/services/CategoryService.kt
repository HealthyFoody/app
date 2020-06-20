package com.healthyfoody.app.services

import com.healthyfoody.app.models.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CategoryService {

    @GET("/menu/categories")
    fun findAll(@Header("Content-Type") contentType : String,@Header("Authorization")token :String): Call<List<Category>>

    @GET("/menu/categories/{id}")
    fun findCategory(@Path("id") id: String): Call<Category>

}