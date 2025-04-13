package com.example.mobile.product_cart_order


import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.codereset.RequestCodeResetRequest
import com.example.mobile.auth.login.LoginRequest
import com.example.mobile.auth.login.LoginResponse
import com.example.mobile.core.utilites.CoreUtils.getErrorMsg
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.details.ProductDetailsResponse
import com.example.mobile.product_cart_order.details.RelatedProductResponse
import com.example.mobile.product_cart_order.home.AllCategoriesResponse
import com.example.mobile.product_cart_order.home.AllProductsInCategoryResponse
import com.example.mobile.product_cart_order.home.ProductListingResponse
import com.example.mobile.product_cart_order.home.ProductSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

class ProductCartOrderRepository @Inject constructor(private val productCartOrderDataSource: ProductCartOrderDataSource) {
    companion object {
        const val TAG = "ProductCartOrderRepository"
    }

    fun getProductListing(recentView: String): Flow<ResourceState<ProductListingResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.getProductListing(recentView)

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun getAllCategories(): Flow<ResourceState<AllCategoriesResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.getAllCategories()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun getProductDetails(productId: String): Flow<ResourceState<ProductDetailsResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.getProductDetails(productId)

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun searchProductByName(name: String): Flow<ResourceState<ProductSearchResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.searchProductByName(name)

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun getAllProductsInCategory(categoryId: String): Flow<ResourceState<AllProductsInCategoryResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.getAllProductsInCategory(categoryId)

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }

    fun getRelatedProducts(
        productId: String,
        location: String? = null,
        maxPrice: Float? = null,
        categoryId: String? = null
    ): Flow<ResourceState<RelatedProductResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = productCartOrderDataSource.getRelatedProducts(
                productId = productId,
                location = location,
                maxPrice = maxPrice,
                categoryId = categoryId
            )

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                when (response.code()) {
                    400 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Bad request"))
                    }

                    422 -> {
                        val errorMsg = getErrorMsg(errorBody = response.errorBody()!!)
                        emit(ResourceState.Error(errorMsg ?: "Unprocessable entity"))
                    }

                    401 -> emit(ResourceState.Error("Unauthorized: Check API key or token"))
                    403 -> emit(ResourceState.Error("Forbidden: Access denied, Insufficient permissions"))
                    404 -> emit(ResourceState.Error("Not Found: Resource unavailable"))
                    500 -> emit(ResourceState.Error("Server Error: Try again later"))
                    else -> emit(ResourceState.Error("Error: Something went wrong"))
                }
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Something went wrong in flow"
                )
            )
        }
    }
}