package com.example.mobile.core.auth

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Add the `Authorization` header to each and every requests
 * */
class AuthInterceptor(private val token:String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                if (!token.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()
        return chain.proceed(request)
    }
}

/**
 * Deletes shared preference if a forbidden (403) response is gotten
 * */
class AuthDeletePreferenceOnBadToken(private val onRemovePreference: () -> Unit): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 403) {
            onRemovePreference()
        }
        return response
    }
}