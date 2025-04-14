package com.example.mobile.product_cart_order.entity

import com.google.gson.annotations.SerializedName

enum class ProductDiscountType{
    FIXED,
    UNIT
}

data class ProductListingDetailsData(
    val id: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_description")
    val productDescription: String,
    @SerializedName("product_price")
    val productPrice: String,
    @SerializedName("product_discount")
    val productDiscount: String,
    @SerializedName("product_discount_type")
    val productDiscountType: ProductDiscountType,
    @SerializedName("product_quantity")
    val productQuantity: Int,
    val slug: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val location: String,
    @SerializedName("max_age_range")
    val maxAgeRange: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("selling_price")
    val sellingPrice: String,
    @SerializedName("avg_rating")
    val avgRating: Float,
    @SerializedName("is_rated")
    val isRated: Boolean,
    @SerializedName("rating_given")
    val ratingGiven: Float
)
