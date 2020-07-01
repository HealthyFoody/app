package com.healthyfoody.app.services

import com.healthyfoody.app.models.Category
import com.healthyfoody.app.models.GoogleMapsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GoogleMapsApiService {
    @GET("/maps/api/geocode/json")
    fun getAddress(@Query("latlng")latlng:String,@Query("key")key:String): Call<GoogleMapsResponse>
}