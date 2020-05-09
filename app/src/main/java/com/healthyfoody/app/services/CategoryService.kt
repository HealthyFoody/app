package com.healthyfoody.app.services

import com.healthyfoody.app.models.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryService {

    @GET("menu/categories")
    fun findAll(): Call<List<Category>>

    @GET("menu/categories/{id}")
    fun findCategory(@Path("id") id: String): Call<Category>

}