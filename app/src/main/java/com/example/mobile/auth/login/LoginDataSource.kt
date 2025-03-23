package com.example.mobile.auth.login


import retrofit2.Call
import javax.inject.Inject

interface LoginDataSourceInterface {
    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse>
}

class LoginDataSource @Inject constructor(private val loginApiService: LoginApiService): LoginDataSourceInterface {
    override fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return loginApiService.loginUser(loginRequest=loginRequest)
    }
}