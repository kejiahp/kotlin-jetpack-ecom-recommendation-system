package com.example.mobile.auth.signup

import android.util.Log
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.core.utilites.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.internal.threadName
import org.json.JSONObject
import retrofit2.Response
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
//            val response = signUpDataSource.registerUser(userData)
            val response = signUpDataSource.registerUser(userData).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {

                if (response.errorBody() != null) {
                    // TODO: Remember to remove the error output
//                    Log.e(TAG + "1", response.errorBody()?.string() ?: "OOps")
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string());
                    val errorMsg = jsonObject.getJSONObject("detail").getString("message");
                    Log.e(TAG + "2", errorMsg ?: "OOps")
                    emit(ResourceState.Error( errorMsg?: "Something went wrong"))
                } else {
                    emit(ResourceState.Error("Something went wrong"))
                }


                // Handle different error codes
//                when (response.code()) {
//                    400 -> onResult("Bad Request: ${response.errorBody()?.string()}")
//                    401 -> onResult("Unauthorized: Check API key or token")
//                    404 -> onResult("Not Found: Resource unavailable")
//                    500 -> onResult("Server Error: Try again later")
//                    else -> onResult("Error ${response.code()}: ${response.errorBody()?.string()}")
//                }


            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Something went wrong in flow"))
        }
    }


//    suspend fun getData(name: String): Response<CountryResponse> {
//        return signUpDataSource.getData(name = name)
//    }
}

