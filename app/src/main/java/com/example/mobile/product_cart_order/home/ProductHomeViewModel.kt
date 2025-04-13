package com.example.mobile.product_cart_order.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.codereset.RequestCodeResetRequest
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.ProdCartOrderSharedPreferenceService
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductHomeViewModel @Inject constructor(
    private val productCartOrderRepository: ProductCartOrderRepository,
    private val prodCartOrderSharedPreferenceService: ProdCartOrderSharedPreferenceService
) :
    ViewModel() {
    companion object {
        const val TAG = "ProductHomeViewModel"
    }

    /**Product listing state*/
    private val _productListingQueryState: MutableStateFlow<DataQueryState<ProductListingResponse>> =
        MutableStateFlow(
            DataQueryState<ProductListingResponse>()
        )
    val productListingQueryState = _productListingQueryState.asStateFlow()

    private val _allCategories: MutableStateFlow<DataQueryState<AllCategoriesResponse>> =
        MutableStateFlow(DataQueryState())
    val allCategories = _allCategories.asStateFlow()

    private val _productSearchResults: MutableStateFlow<DataQueryState<ProductSearchResponse>> =
        MutableStateFlow(DataQueryState())
    val productSearchResults = _productSearchResults.asStateFlow()

    init {
        val recentViewList = prodCartOrderSharedPreferenceService.getRecentViewFromPreference()

        getProductListing(if (recentViewList.isNotEmpty()) recentViewList.joinToString(separator = ",") else "")
        getAllCategories()
    }

    fun addToRecentlyViewed(productId: String) {
        val recentViewList = prodCartOrderSharedPreferenceService.getRecentViewFromPreference().toMutableList()
        if (recentViewList.size < 3) {
            recentViewList.add(productId)
            prodCartOrderSharedPreferenceService.saveRecentViewToPreference(recentViewList)
        }
    }

    fun searchProductByName(name: String) {
        if (name.isNotEmpty() && name.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                productCartOrderRepository.searchProductByName(name)
                    .collectLatest { it ->
                        when (it) {
                            is ResourceState.Pending -> {
                                _productSearchResults.value =
                                    _productSearchResults.value.copy(
                                        isLoading = false,
                                        data = null,
                                        errorMsg = ""
                                    )
                            }

                            is ResourceState.Loading -> {
                                _productSearchResults.value =
                                    _productSearchResults.value.copy(
                                        isLoading = true
                                    )
                            }

                            is ResourceState.Success -> {
                                _productSearchResults.value =
                                    _productSearchResults.value.copy(
                                        isLoading = false,
                                        data = it.data,
                                        errorMsg = ""
                                    )
                            }

                            is ResourceState.Error -> {
                                _productSearchResults.value =
                                    _productSearchResults.value.copy(
                                        isLoading = false,
                                        errorMsg = it.error.toString()
                                    )
                            }
                        }
                    }
            }
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getAllCategories()
                .collectLatest { allCategoriesRes ->
                    when (allCategoriesRes) {
                        is ResourceState.Pending -> {
                            _allCategories.value =
                                _allCategories.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _allCategories.value =
                                _allCategories.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _allCategories.value =
                                _allCategories.value.copy(
                                    isLoading = false,
                                    data = allCategoriesRes.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _allCategories.value =
                                _allCategories.value.copy(
                                    isLoading = false,
                                    errorMsg = allCategoriesRes.error.toString()
                                )
                        }
                    }
                }
        }
    }

    private fun getProductListing(recentView: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getProductListing(recentView)
                .collectLatest { productListRes ->
                    when (productListRes) {
                        is ResourceState.Pending -> {
                            _productListingQueryState.value =
                                _productListingQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _productListingQueryState.value =
                                _productListingQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _productListingQueryState.value =
                                _productListingQueryState.value.copy(
                                    isLoading = false,
                                    data = productListRes.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _productListingQueryState.value =
                                _productListingQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = productListRes.error.toString()
                                )
                        }
                    }
                }
        }
    }

}