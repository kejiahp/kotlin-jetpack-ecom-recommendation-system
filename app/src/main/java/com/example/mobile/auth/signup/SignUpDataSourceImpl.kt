package com.example.mobile.auth.signup;

import retrofit2.Call
import javax.inject.Inject

/**
 * This will serve as a means to access the underlying API Service
 * */
class SignUpDataSourceImpl @Inject constructor(private val signUpApiService: SignUpApiService): SignUpDataSource {
//    override suspend fun getData(name: String): Response<CountryResponse> {
//        return signUpApiService.getData(name=name)
//    }

    override fun registerUser(registerUserData: RegisterRequest): Call<RegisterResponse> {
        return signUpApiService.registerUser(registerUserData=registerUserData)
    }
}
