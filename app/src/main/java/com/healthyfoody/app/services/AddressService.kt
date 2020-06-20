package com.healthyfoody.app.services

import com.healthyfoody.app.models.Address
import retrofit2.Call
import retrofit2.http.*

interface AddressService {
    @GET("/location/addresses")
    fun findAddressesByIdCustomer(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Query("customer") customerId:String):Call<List<Address>>

    @DELETE("/location/addresses/{id}")
    fun deleteAddressById(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Path("id")id :String):Call<Void>

    @POST("/location/addresses?default=false")
    fun addAddress(@Header("Content-Type") contentType : String, @Header("Authorization")token :String,@Body address:Address):Call<Address>
}