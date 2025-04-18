package com.example.mobile.product_cart_order.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import com.example.mobile.product_cart_order.entity.AddToCartRequest
import com.example.mobile.product_cart_order.entity.CartResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val productCartOrderRepository: ProductCartOrderRepository) :
    ViewModel() {
    private val _productDetailsQueryState: MutableStateFlow<DataQueryState<ProductDetailsResponse>> =
        MutableStateFlow(DataQueryState<ProductDetailsResponse>())
    val productDetailsQueryState = _productDetailsQueryState.asStateFlow()

    private val _relatedProductQueryState: MutableStateFlow<DataQueryState<RelatedProductResponse>> =
        MutableStateFlow(DataQueryState())
    val relatedProductQueryState = _relatedProductQueryState.asStateFlow()

    private val _addToCartQueryState: MutableStateFlow<DataQueryState<CartResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val addToCartQueryState = _addToCartQueryState.asStateFlow()

    fun addToCartHandler(prodId: String, quantity: Int, action: String) {
        val payload = AddToCartRequest(productId = prodId, quantity = quantity, action = action)

        addToCart(payload)
    }

    private fun addToCart(productDetails: AddToCartRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.addToCart(
                productDetails
            )
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _addToCartQueryState.value =
                                _addToCartQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _addToCartQueryState.value =
                                _addToCartQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _addToCartQueryState.value =
                                _addToCartQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _addToCartQueryState.value =
                                _addToCartQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }


    fun getRelatedProducts(
        productId: String,
        location: String? = null,
        maxPrice: Float? = null,
        categoryId: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getRelatedProducts(
                productId,
                location = location,
                maxPrice = maxPrice,
                categoryId = categoryId
            )
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _relatedProductQueryState.value =
                                _relatedProductQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _relatedProductQueryState.value =
                                _relatedProductQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _relatedProductQueryState.value =
                                _relatedProductQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _relatedProductQueryState.value =
                                _relatedProductQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }


    fun getProductDetails(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getProductDetails(productId)
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _productDetailsQueryState.value =
                                _productDetailsQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _productDetailsQueryState.value =
                                _productDetailsQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _productDetailsQueryState.value =
                                _productDetailsQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _productDetailsQueryState.value =
                                _productDetailsQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }

}