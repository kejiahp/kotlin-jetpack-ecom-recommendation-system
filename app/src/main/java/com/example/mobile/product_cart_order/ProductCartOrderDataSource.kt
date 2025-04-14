package com.example.mobile.product_cart_order

import com.example.mobile.product_cart_order.details.ProductDetailsResponse
import com.example.mobile.product_cart_order.details.RelatedProductResponse
import com.example.mobile.product_cart_order.entity.AddToCartRequest
import com.example.mobile.product_cart_order.entity.CartResponse
import com.example.mobile.product_cart_order.entity.CheckoutResponse
import com.example.mobile.product_cart_order.entity.GetAllOrdersResponse
import com.example.mobile.product_cart_order.entity.GetUserCartResponse
import com.example.mobile.product_cart_order.entity.RateProductRequest
import com.example.mobile.product_cart_order.entity.RateProductResponse
import com.example.mobile.product_cart_order.home.AllCategoriesResponse
import com.example.mobile.product_cart_order.home.AllProductsInCategoryResponse
import com.example.mobile.product_cart_order.home.ProductListingResponse
import com.example.mobile.product_cart_order.home.ProductSearchResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

interface ProductCartOrderDataSourceInterface {
    suspend fun getProductListing(recentView: String): Response<ProductListingResponse>

    suspend fun getAllCategories(): Response<AllCategoriesResponse>

    suspend fun getProductDetails(productId: String): Response<ProductDetailsResponse>

    suspend fun searchProductByName(name: String): Response<ProductSearchResponse>

    suspend fun getAllProductsInCategory(categoryId: String): Response<AllProductsInCategoryResponse>

    suspend fun getRelatedProducts(
        productId: String,
        location: String? = null,
        maxPrice: Float? = null,
        categoryId: String? = null
    ): Response<RelatedProductResponse>

    fun addToCart(productDetails: AddToCartRequest): Call<CartResponse>

    fun removeItemFromCart(productId: String): Call<CartResponse>

    fun emptyCart(): Call<CartResponse>

    suspend fun getUserCart(): Response<GetUserCartResponse>

    fun checkout(receiptEmail: String?): Call<CheckoutResponse>

    suspend fun getAllUserOrder(): Response<GetAllOrdersResponse>

    fun addProductRating(rateProductRequest: RateProductRequest): Call<RateProductResponse>
}


class ProductCartOrderDataSource @Inject constructor(private val productCartOrderApiService: ProductCartOrderApiService) :
    ProductCartOrderDataSourceInterface {
    override suspend fun getProductListing(recentView: String): Response<ProductListingResponse> {
        return productCartOrderApiService.getProductListing(recentView = recentView)
    }

    override suspend fun getAllCategories(): Response<AllCategoriesResponse> {
        return productCartOrderApiService.getAllCategories()
    }

    override suspend fun getProductDetails(productId: String): Response<ProductDetailsResponse> {
        return productCartOrderApiService.getProductDetails(productId)
    }

    override suspend fun searchProductByName(name: String): Response<ProductSearchResponse> {
        return productCartOrderApiService.searchProductByName(name = name)
    }

    override suspend fun getAllProductsInCategory(categoryId: String): Response<AllProductsInCategoryResponse> {
        return productCartOrderApiService.getAllProductsInCategory(categoryId)
    }

    override suspend fun getRelatedProducts(
        productId: String,
        location: String?,
        maxPrice: Float?,
        categoryId: String?
    ): Response<RelatedProductResponse> {
        return productCartOrderApiService.getRelatedProducts(
            productId = productId,
            location = location,
            maxPrice = maxPrice,
            categoryId = categoryId
        )
    }

    override fun addToCart(productDetails: AddToCartRequest): Call<CartResponse> {
        return productCartOrderApiService.addToCart(productDetails)
    }

    override fun removeItemFromCart(productId: String): Call<CartResponse> {
        return productCartOrderApiService.removeItemFromCart(productId)
    }

    override fun emptyCart(): Call<CartResponse> {
        return productCartOrderApiService.emptyCart()
    }

    override suspend fun getUserCart(): Response<GetUserCartResponse> {
        return productCartOrderApiService.getUserCart()
    }

    override fun checkout(receiptEmail: String?): Call<CheckoutResponse> {
        return productCartOrderApiService.checkout(receiptEmail=receiptEmail)
    }

    override suspend fun getAllUserOrder(): Response<GetAllOrdersResponse> {
        return productCartOrderApiService.getAllUserOrder()
    }

    override fun addProductRating(rateProductRequest: RateProductRequest): Call<RateProductResponse> {
        return productCartOrderApiService.addProductRating(rateProductRequest)
    }
}