package com.example.mobile.product_cart_order.entity

import com.example.mobile.core.ApiResponse
import com.example.mobile.product_cart_order.details.ProductRatingData
import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("product_id")
    val productId: String,
    val quantity: Int,
    val action: String
)

data class CartItemData(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)

data class CartItemPopulatedData(
    @SerializedName("product_id")
    val productId: ProductListingDetailsData,
    @SerializedName("quantity")
    val quantity: Int
)

data class CartData<T>(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("cart_items")
    val cartItems: List<T>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class OrderData<T>(
    val id: String,
    @SerializedName("order_no")
    val orderNo: String,
    @SerializedName("order_total")
    val orderTotal: String,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("order_item")
    val orderItem: List<T>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class RateProductRequest(
    @SerializedName("product_id")
    val productId: String,
    val rating: Int
)


typealias CartResponse = ApiResponse<CartData<CartItemData>>
typealias GetUserCartResponse = ApiResponse<CartData<CartItemPopulatedData>>
typealias CheckoutResponse = ApiResponse<OrderData<CartItemData>>
typealias GetAllOrdersResponse = ApiResponse<List<OrderData<CartItemPopulatedData>>>
typealias RateProductResponse = ApiResponse<ProductRatingData<String>>