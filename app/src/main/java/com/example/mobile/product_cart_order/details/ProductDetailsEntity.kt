package com.example.mobile.product_cart_order.details

import com.example.mobile.core.ApiResponse
import com.example.mobile.product_cart_order.entity.CategoryData
import com.example.mobile.product_cart_order.entity.ProductDiscountType
import com.example.mobile.product_cart_order.entity.ProductListingDetailsData
import com.google.gson.annotations.SerializedName

data class PublicUserData(
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

data class ProductRatingData<T>(
    val id: String,
    @SerializedName("user_id")
    val userId: T,
    @SerializedName("product_id")
    val productId: String,
    val rating: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ProductDetailsData(
    val id: String,
    @SerializedName("category_id")
    val categoryId: CategoryData,
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
    @SerializedName("product_ratings")
    val productRatings: List<ProductRatingData<PublicUserData>>
)

typealias ProductDetailsResponse = ApiResponse<ProductDetailsData>
typealias RelatedProductResponse = ApiResponse<List<ProductListingDetailsData>>