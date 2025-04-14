package com.example.mobile.product_cart_order.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import com.example.mobile.product_cart_order.entity.GetAllOrdersResponse
import com.example.mobile.product_cart_order.entity.RateProductRequest
import com.example.mobile.product_cart_order.entity.RateProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val productCartOrderRepository: ProductCartOrderRepository) :
    ViewModel() {

    private val _usersOrdersQueryState: MutableStateFlow<DataQueryState<GetAllOrdersResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val usersOrdersQueryState = _usersOrdersQueryState.asStateFlow()

    private val _rateProductQueryState: MutableStateFlow<DataQueryState<RateProductResponse>> =
        MutableStateFlow(DataQueryState())
    val rateProductQueryState = _rateProductQueryState.asStateFlow()

    init {
        getAllUserOrder()
    }

    fun onRatingHandler(productId: String, ratingNumber: Int) {
        val ratingReq = RateProductRequest(productId = productId, rating = ratingNumber)
        addProductRating(ratingReq)
    }

    private fun addProductRating(ratingRequest: RateProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.addProductRating(ratingRequest)
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _rateProductQueryState.value =
                                _rateProductQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _rateProductQueryState.value =
                                _rateProductQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _rateProductQueryState.value =
                                _rateProductQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _rateProductQueryState.value =
                                _rateProductQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }


    fun getAllUserOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getAllUserOrder()
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _usersOrdersQueryState.value =
                                _usersOrdersQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _usersOrdersQueryState.value =
                                _usersOrdersQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _usersOrdersQueryState.value =
                                _usersOrdersQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _usersOrdersQueryState.value =
                                _usersOrdersQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }
}