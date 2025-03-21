package com.example.mobile.auth.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


// This defines the API Services related to the SignUp Screen
interface SignUpApiService {
    @POST("/user/sign-up")
    fun registerUser(@Body registerUserData: RegisterRequest): Call<RegisterResponse>

//    @GET("/")
//    suspend fun getData(@Query("name") name: String): Response<CountryResponse>
}