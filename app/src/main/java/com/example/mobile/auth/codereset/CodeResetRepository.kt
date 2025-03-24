package com.example.mobile.auth.codereset

import com.example.mobile.core.utilites.CoreUtils.getErrorMsg
import com.example.mobile.core.utilites.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import retrofit2.awaitResponse
import javax.inject.Inject

class CodeResetRepository @Inject constructor(private val codeResetDataSource: CodeResetDataSource) {
    companion object {
        private const val TAG = "CodeResetRepository"
    }

    fun requestCodeResetCode(requestCodeResetRequest: RequestCodeResetRequest): Flow<ResourceState<RequestCodeResponse>> {
        return flow{
            emit(ResourceState.Loading())

            val response = codeResetDataSource.requestCodeResetCode(requestCodeResetRequest).awaitResponse();

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }

        }.catch{e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun codeResetService(codeResetRequest: CodeResetRequest): Flow<ResourceState<CodeResetResponse>> {
        return flow{
            emit(ResourceState.Loading())

            val response = codeResetDataSource.codeResetService(codeResetRequest).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }

        }.catch{e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }
}