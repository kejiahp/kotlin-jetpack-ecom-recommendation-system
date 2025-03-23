package com.example.mobile.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.utilites.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormState(
    val username: String = "",
    val usernameError: String? = null,
    val code: String = "",
    val codeError: String? = null,
    val isValid: Boolean = false
)


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {
    companion object {
        const val TAG = "LoginViewModel"
    }

    /** Login Form State */
    private val _formState: MutableStateFlow<FormState> = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()

    /** Login Response State */
    private val _resData: MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
    val resData = _resData.asStateFlow()

    /** Loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /** Error message */
    private val _errorMsg: MutableStateFlow<String> = MutableStateFlow("")
    val errorMsg = _errorMsg.asStateFlow()

    /** controls changes in username */
    fun onUsernameChangeHandler(text: String) {
        _formState.update { oldState ->
            oldState.copy(
                username = text.lowercase().replace(" ", "_")
            )
        }
    }

    /** controls changes in code */
    fun onCodeChangeHandler(code: String) {
        _formState.update { oldState ->
            oldState.copy(
                code = code
            )
        }
    }

    /**
     * Validate username
     *
     * Checking if it is empty
     * */
    private fun validateUsername(username: String): String? {
        return if (username.isEmpty()) "Username cannot be empty"
        else null
    }

    /**
     * Validate code
     *
     * Checking if code is six digits
     * */
    private fun validateCode(code: String): String? {
        if (_formState.value.code.isEmpty() || _formState.value.code.length != 6) {
            return "code must be a 6-digit number"
        }
        val code = code.toInt()
        return if (!(100000 <= code && code <= 999999)) "Code must be a 6-digit number"
        else null
    }

    fun onLoginHandler() {
        val usernameError = validateUsername(_formState.value.username)
        val codeError = validateCode(_formState.value.code)

        _formState.value = _formState.value.copy(
            usernameError = usernameError,
            codeError = codeError,
            isValid = usernameError == null && codeError == null
        )

        if (_formState.value.isValid) {
            val loginRequest = LoginRequest(
                username = _formState.value.username,
                code = _formState.value.code.toInt()
            )
            loginUser(loginRequest)
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.loginUser(loginRequest).collectLatest { loginRes ->
                when (loginRes) {
                    is ResourceState.Pending -> {
                        _isLoading.value = false
                        _resData.value = null
                        _errorMsg.value = ""
                    }

                    is ResourceState.Loading -> {
                        _isLoading.value = true
                    }

                    is ResourceState.Success -> {
                        _isLoading.value = false
                        _resData.value = loginRes.data
                        _errorMsg.value = ""
                    }

                    is ResourceState.Error -> {
                        _isLoading.value = false
                        _errorMsg.value = loginRes.error.toString()
                    }
                }
            }
        }
    }
}