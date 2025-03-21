package com.example.mobile.auth.signup

import retrofit2.Call
import retrofit2.Response

/**
 * Interface guiding the `SignUpDataSourceImpl`
 * */
interface SignUpDataSource {
    fun registerUser(registerUserData: RegisterRequest): Call<RegisterResponse>

//    suspend fun getData(name: String): Response<registerUser>
}