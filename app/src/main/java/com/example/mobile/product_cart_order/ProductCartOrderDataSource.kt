package com.example.mobile.product_cart_order

import com.example.mobile.product_cart_order.details.ProductDetailsResponse
import com.example.mobile.product_cart_order.details.RelatedProductResponse
import com.example.mobile.product_cart_order.home.AllCategoriesResponse
import com.example.mobile.product_cart_order.home.AllProductsInCategoryResponse
import com.example.mobile.product_cart_order.home.ProductListingResponse
import com.example.mobile.product_cart_order.home.ProductSearchResponse
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
}