package com.example.mobile.product_cart_order

import com.example.mobile.product_cart_order.details.ProductDetailsResponse
import com.example.mobile.product_cart_order.details.RelatedProductResponse
import com.example.mobile.product_cart_order.home.AllCategoriesResponse
import com.example.mobile.product_cart_order.home.AllProductsInCategoryResponse
import com.example.mobile.product_cart_order.home.ProductListingResponse
import com.example.mobile.product_cart_order.home.ProductSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductCartOrderApiService {
    @GET("/product/home-product-listing")
    suspend fun getProductListing(@Query("recent_view") recentView: String): Response<ProductListingResponse>

    @GET("/product/all-categories")
    suspend fun getAllCategories(): Response<AllCategoriesResponse>

    @GET("/product/{product_id}")
    suspend fun getProductDetails(@Path("product_id") productId: String): Response<ProductDetailsResponse>

    @GET("/product/search/")
    suspend fun searchProductByName(@Query("name") name: String): Response<ProductSearchResponse>

    @GET("/product/get-product-by-category/{category_id}")
    suspend fun getAllProductsInCategory(@Path("category_id") categoryId: String): Response<AllProductsInCategoryResponse>

    @GET("/product/get-related-products/{product_id}")
    suspend fun getRelatedProducts(
        @Path("product_id") productId: String,
        @Query("location") location: String?,
        @Query("max_price") maxPrice: Float?,
        @Query("category_id") categoryId: String?
    ): Response<RelatedProductResponse>
}