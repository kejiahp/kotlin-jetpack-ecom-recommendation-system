package com.example.mobile.auth.login

import com.example.mobile.core.ApiResponse
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val code: Int
)

data class LoginData(
    val id: String,
    val username: String,
    val location: String,
    val age: Int,
    val gender: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updateAt: String,
    val tkn: String
)

typealias LoginResponse = ApiResponse<LoginData>