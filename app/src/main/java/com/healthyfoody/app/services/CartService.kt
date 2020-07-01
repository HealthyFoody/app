package com.healthyfoody.app.services

import com.healthyfoody.app.models.Cart
import com.healthyfoody.app.models.CartMealRequest
import retrofit2.Call
import retrofit2.http.*

interface CartService {
    @GET("/cart/details")
    fun getCart(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Query("customer")customer:String):Call<Cart>

    @POST("/cart/{id}/meal/")
    fun addMealCart(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Path("id")id:String,@Body request: CartMealRequest):Call<Void>

    @HTTP(method = "DELETE", path = "/cart/{id}/remove", hasBody = true)
    fun deleteItemFromCart(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Path("id")id:String,@Body request: CartMealRequest):Call<Cart>


    @DELETE("/cart/{id}/clear")
    fun clearCart(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Path("id")id:String):Call<Void>
}