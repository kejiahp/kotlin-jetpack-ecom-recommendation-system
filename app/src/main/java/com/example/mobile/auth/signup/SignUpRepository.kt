package com.example.mobile.auth.signup

import com.example.mobile.core.utilites.CoreUtils.getErrorMsg
import com.example.mobile.core.utilites.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

/**
 * This is the actual means to access the underlying API Service, unlike the `SignUpDataSourceImpl` which just serves as a implementation of `SignUpDataSource`
 *
 * This will also be used to manage the state of the request
 * */
class SignUpRepository @Inject constructor(private val signUpDataSource: SignUpDataSource) {
    companion object {
        const val TAG: String = "SignUpRepository"
    }

    fun registerUser(userData: RegisterRequest): Flow<ResourceState<RegisterResponse>> {
        return flow {
            emit(ResourceState.Loading())
            val response = signUpDataSource.registerUser(userData).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error( errorMsg?: "Bad request"))
                    }
                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error( errorMsg?: "Unprocessable entity"))
                    }
                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Something went wrong in flow"))
        }
    }



//    suspend fun getData(name: String): Response<CountryResponse> {
//        return signUpDataSource.getData(name = name)
//    }
}

