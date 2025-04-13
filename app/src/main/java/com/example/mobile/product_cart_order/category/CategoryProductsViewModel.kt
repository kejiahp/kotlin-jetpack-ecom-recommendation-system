package com.example.mobile.product_cart_order.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import com.example.mobile.product_cart_order.home.AllProductsInCategoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryProductsViewModel @Inject constructor(private val productCartOrderRepository: ProductCartOrderRepository): ViewModel() {
    companion object {
        const val TAG = "CategoryProductsViewModel"
    }

    private val _allCategoryProductQueryState: MutableStateFlow<DataQueryState<AllProductsInCategoryResponse>> =
        MutableStateFlow(DataQueryState())
    val allCategoryProductQueryState = _allCategoryProductQueryState.asStateFlow()


    fun getAllProductsInCategory(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getAllProductsInCategory(categoryId)
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _allCategoryProductQueryState.value =
                                _allCategoryProductQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _allCategoryProductQueryState.value =
                                _allCategoryProductQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _allCategoryProductQueryState.value =
                                _allCategoryProductQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _allCategoryProductQueryState.value =
                                _allCategoryProductQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }
}