package com.example.mobile.auth.codereset

import retrofit2.Call
import javax.inject.Inject

interface CodeResetDataSourceInterface {
    fun requestCodeResetCode(requestCodeResetRequest: RequestCodeResetRequest): Call<RequestCodeResponse>

    fun codeResetService(codeResetRequest: CodeResetRequest): Call<CodeResetResponse>
}

class CodeResetDataSource @Inject constructor(private val codeResetApiService: CodeResetApiService): CodeResetDataSourceInterface {
    override fun requestCodeResetCode(requestCodeResetRequest: RequestCodeResetRequest): Call<RequestCodeResponse> {
        return codeResetApiService.requestCodeResetCode(requestCodeResetRequest)
    }
    override fun codeResetService(codeResetRequest: CodeResetRequest): Call<CodeResetResponse> {
        return codeResetApiService.codeResetService(codeResetRequest)
    }
}