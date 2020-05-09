package com.healthyfoody.app.services

import com.healthyfoody.app.models.UserRequest
import com.healthyfoody.app.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @POST("/users/register")
    fun registerUser(@Header("Accept") contentType : String,@Body userRequest: UserRequest): Call<UserResponse>

    @POST("/users/login")
    fun loginUser(@Body userRequest : UserRequest)
}