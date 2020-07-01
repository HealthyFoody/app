package com.healthyfoody.app.services


import com.healthyfoody.app.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("/menu/products")
    fun findAll(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Query("category")categoryId : String): Call<List<Product>>

    @GET("/menu/products/{id}")
    fun findById(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Path("id")productId :String): Call<Product>

}