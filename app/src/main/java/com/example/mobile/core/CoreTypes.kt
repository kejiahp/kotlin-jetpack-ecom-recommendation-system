package com.example.mobile.core

import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    val success: Boolean,
    val data: T
)

data class ErrorResponseDetails(
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    val success: Boolean,
)

data class ErrorResponse(
    val detail: ErrorResponseDetails
)