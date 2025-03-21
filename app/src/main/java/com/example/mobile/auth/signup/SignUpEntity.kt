package com.example.mobile.auth.signup;

import com.example.mobile.core.ApiResponse
import com.google.gson.annotations.SerializedName

// This data class defines the shape of the JSON object
//data class CountryItem(val country_id: String, val probability: Double)
//data class CountryResponse(val count: Number, val name: String, val country: List<CountryItem>)


// This data class defines the shape of the JSON object
data class RegisterData(
    val id: String,
    val username: String,
    val location: String,
    val age: Int,
    val gender: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

// This data class defines the shape of the JSON object
data class RegisterRequest(
    val email: String,
    val username: String,
    val location: String,
    val age: Int,
    val gender: String
)

// to avoid repetition, we use a typealias
typealias RegisterResponse = ApiResponse<RegisterData>