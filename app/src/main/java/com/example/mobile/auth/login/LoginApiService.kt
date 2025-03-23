package com.example.mobile.auth.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/** Defines api service related to the login screen*/
interface LoginApiService {
    @POST("/user/sign-in")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
}