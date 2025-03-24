package com.example.mobile.auth.codereset

import com.example.mobile.core.ApiResponse
import com.google.gson.annotations.SerializedName

data class RequestCodeResetRequest(
    val username: String,
    val email: String
)

data class CodeResetRequest(
    @SerializedName("new_code")
    val newCode: Int,
    @SerializedName("old_code")
    val oldCode: Int,
    @SerializedName("reset_token")
    val resetToken: String
)

typealias RequestCodeResponse = ApiResponse<Nothing>

typealias CodeResetResponse = ApiResponse<Nothing>