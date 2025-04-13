package com.example.mobile.product_cart_order.home

import com.example.mobile.core.ApiResponse
import com.example.mobile.product_cart_order.entity.CategoryData
import com.example.mobile.product_cart_order.entity.ProductListingDetailsData
import com.google.gson.annotations.SerializedName


data class ProductListingData(
    @SerializedName("new_added")
    val newAdded: List<ProductListingDetailsData>,
    val trending: List<ProductListingDetailsData>,
    @SerializedName("similar_to_recent_view")
    val similarToRecentView: List<ProductListingDetailsData>,
    val explore: List<ProductListingDetailsData>,
    @SerializedName("same_location")
    val sameLocation: List<ProductListingDetailsData>,
    @SerializedName("age_range")
    val ageRange: List<ProductListingDetailsData>,
    @SerializedName("might_interest_you")
    val mightInterestYou: List<ProductListingDetailsData>
)

data class AllCategoriesData(
    val categories: List<CategoryData>
)

data class AllProductsInCategoryData(
    val id: String,
    val name: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val products: List<ProductListingDetailsData>
)


typealias ProductListingResponse = ApiResponse<ProductListingData>
typealias AllCategoriesResponse = ApiResponse<AllCategoriesData>
typealias ProductSearchResponse = ApiResponse<List<ProductListingDetailsData>>
typealias AllProductsInCategoryResponse = ApiResponse<AllProductsInCategoryData>