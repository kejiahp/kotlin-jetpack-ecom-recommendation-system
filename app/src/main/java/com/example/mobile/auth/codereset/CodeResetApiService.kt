package com.example.mobile.auth.codereset

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Contains api service for code reset operations
 * */
interface CodeResetApiService {
    @POST("/user/request-code-reset-token")
    fun requestCodeResetCode(@Body requestCodeResetRequest: RequestCodeResetRequest): Call<RequestCodeResponse>

    @POST("/user/code-reset")
    fun codeResetService(@Body codeResetRequest: CodeResetRequest): Call<CodeResetResponse>
}