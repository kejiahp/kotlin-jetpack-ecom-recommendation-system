package com.example.mobile.core

import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    val success: Boolean,
    val data: T
)

data class DataQueryState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val errorMsg: String = ""
)