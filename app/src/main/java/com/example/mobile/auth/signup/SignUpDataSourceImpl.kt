package com.example.mobile.auth.signup;

import retrofit2.Call
import javax.inject.Inject

/**
 * This will serve as a means to access the underlying API Service
 * */
class SignUpDataSourceImpl @Inject constructor(private val signUpApiService: SignUpApiService): SignUpDataSource {
    override fun registerUser(registerUserData: RegisterRequest): Call<RegisterResponse> {
        return signUpApiService.registerUser(registerUserData=registerUserData)
    }
}
