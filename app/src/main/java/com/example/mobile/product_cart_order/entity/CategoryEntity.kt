package com.example.mobile.product_cart_order.entity

import com.google.gson.annotations.SerializedName

data class CategoryData(
    val id: String,
    val name: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)