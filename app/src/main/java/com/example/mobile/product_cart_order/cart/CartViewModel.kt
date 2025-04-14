package com.example.mobile.product_cart_order.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.ResourceState
import com.example.mobile.product_cart_order.ProductCartOrderRepository
import com.example.mobile.product_cart_order.entity.AddToCartRequest
import com.example.mobile.product_cart_order.entity.CartResponse
import com.example.mobile.product_cart_order.entity.CheckoutResponse
import com.example.mobile.product_cart_order.entity.GetUserCartResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutFormState(
    val email: String = "",
    val emailError: String? = null,
    val isValid: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(private val productCartOrderRepository: ProductCartOrderRepository) :
    ViewModel() {

    companion object {
        const val TAG = "CartViewModel"
    }

    /** User data state */
    private val _receiptFormState: MutableStateFlow<CheckoutFormState> = MutableStateFlow(
        CheckoutFormState()
    )
    val receiptFormState = _receiptFormState.asStateFlow()

    private val _userCartQueryState: MutableStateFlow<DataQueryState<GetUserCartResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val userCartQueryState = _userCartQueryState.asStateFlow()

    private val _removeFromCartQueryState: MutableStateFlow<DataQueryState<CartResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val removeFromCartQueryState = _removeFromCartQueryState.asStateFlow()

    private val _checkoutQueryState: MutableStateFlow<DataQueryState<CheckoutResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val checkoutQueryState = _checkoutQueryState.asStateFlow()

    private val _emptyCartQueryState: MutableStateFlow<DataQueryState<CartResponse>> =
        MutableStateFlow(DataQueryState())
    val emptyCartQueryState = _emptyCartQueryState.asStateFlow()


    private val _addToCartQueryState: MutableStateFlow<DataQueryState<CartResponse>> =
        MutableStateFlow(
            DataQueryState()
        )
    val addToCartQueryState = _addToCartQueryState.asStateFlow()

    init {
        getUserCart()
    }

    /**
     * Validate email
     *
     * Checking if it is empty and follows actual email pattern
     * */
    private fun validateEmail(email: String): String? {
        return if (email.isEmpty()) "Email cannot be empty"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) "Invalid email format"
        else null
    }

    /** controls changes in email */
    fun onEmailChangeHandler(text: String) {
        _receiptFormState.update { oldState -> oldState.copy(email = text) }
    }

    fun addToCartHandler(prodId: String, quantity: Int, action: String) {
        val payload = AddToCartRequest(productId = prodId, quantity = quantity, action=action)

        addToCart(payload)
    }

    fun onCheckoutHandler() {
        val emailError = validateEmail(_receiptFormState.value.email)
        // update the form state with error values
        _receiptFormState.value = _receiptFormState.value.copy(
            emailError = emailError,
            isValid = emailError == null
        )
        // If the form state is valid, initiate register request
        if (_receiptFormState.value.isValid) {
            checkout(_receiptFormState.value.email)
        }
    }

    private fun checkout(receiptEmail: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.checkout(receiptEmail)
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _checkoutQueryState.value =
                                _checkoutQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _checkoutQueryState.value =
                                _checkoutQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _checkoutQueryState.value =
                                _checkoutQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _checkoutQueryState.value =
                                _checkoutQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }

    fun emptyCart() {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.emptyCart()
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _emptyCartQueryState.value =
                                _emptyCartQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _emptyCartQueryState.value =
                                _emptyCartQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _emptyCartQueryState.value =
                                _emptyCartQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _emptyCartQueryState.value =
                                _emptyCartQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }

    fun removeItemFromCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.removeItemFromCart(productId)
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _removeFromCartQueryState.value =
                                _removeFromCartQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _removeFromCartQueryState.value =
                                _removeFromCartQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _removeFromCartQueryState.value =
                                _removeFromCartQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _removeFromCartQueryState.value =
                                _removeFromCartQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
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

    fun getUserCart() {
        viewModelScope.launch(Dispatchers.IO) {
            productCartOrderRepository.getUserCart()
                .collectLatest { it ->
                    when (it) {
                        is ResourceState.Pending -> {
                            _userCartQueryState.value =
                                _userCartQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _userCartQueryState.value =
                                _userCartQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _userCartQueryState.value =
                                _userCartQueryState.value.copy(
                                    isLoading = false,
                                    data = it.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _userCartQueryState.value =
                                _userCartQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = it.error.toString()
                                )
                        }
                    }
                }
        }
    }
}