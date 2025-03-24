package com.example.mobile.auth.codereset

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.DataQueryState
import com.example.mobile.core.utilites.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class RequestCodeResetFormState(
    val username: String = "",
    val usernameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val isValid: Boolean = false
)

data class CodeResetFormState(
    val newCode: String = "",
    val newCodeError: String? = null,
    val oldCode: String = "",
    val oldCodeError: String? = null,
    val resetToken: String = "",
    val resetTokenError: String? = null,
    val isValid: Boolean = false
)

@HiltViewModel
class CodeResetViewModel @Inject constructor(private val codeResetRepository: CodeResetRepository) :
    ViewModel() {
    companion object {
        private const val TAG = "CodeResetViewModel"
    }

    /**
     * Request Code Reset Form State
     * */
    private val _requestCodeResetFormState: MutableStateFlow<RequestCodeResetFormState> =
        MutableStateFlow(RequestCodeResetFormState())
    val requestCodeResetFormState = _requestCodeResetFormState.asStateFlow()

    /**
     * Code Reset Form State
     * */
    private val _codeResetFormState: MutableStateFlow<CodeResetFormState> =
        MutableStateFlow(CodeResetFormState())
    val codeResetFormState = _codeResetFormState.asStateFlow()

    /** Request Code Reset Query State*/
    private val _requestCodeResetQueryState: MutableStateFlow<DataQueryState<RequestCodeResponse>> =
        MutableStateFlow(DataQueryState())
    val requestCodeResetQueryState = _requestCodeResetQueryState.asStateFlow()

    /** Code Reset Query State*/
    private val _codeResetQueryState: MutableStateFlow<DataQueryState<CodeResetResponse>> =
        MutableStateFlow(DataQueryState())
    val codeResetQueryState = _codeResetQueryState.asStateFlow()

    /** controls changes in username */
    fun onRequestCodeUsernameChangeHandler(text: String) {
        _requestCodeResetFormState.value = _requestCodeResetFormState.value.copy(
            username = text
        )
    }

    /** controls changes in email */
    fun onRequestCodeEmailChangeHandler(text: String) {
        _requestCodeResetFormState.value = _requestCodeResetFormState.value.copy(
            email = text
        )
    }

    /** controls changes in new code */
    fun onCodeResetNewCodeChangeHandler(text: String) {
        _codeResetFormState.value = _codeResetFormState.value.copy(
            newCode = text
        )
    }

    /** controls changes in old code */
    fun onCodeResetOldCodeChangeHandler(text: String) {
        _codeResetFormState.value = _codeResetFormState.value.copy(
            oldCode = text
        )
    }

    /** controls changes in token */
    fun onCodeResetTokenChangeHandler(text: String) {
        _codeResetFormState.value = _codeResetFormState.value.copy(
            resetToken = text
        )
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
        if (code.isEmpty() || code.length != 6) {
            return "code must be a 6-digit number"
        }
        val code = code.toInt()
        return if (!(100000 <= code && code <= 999999)) "Code must be a 6-digit number"
        else null
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

    fun onRequestCodeSubmitHandler() {
        val usernameError = validateUsername(_requestCodeResetFormState.value.username)
        val emailError = validateEmail(_requestCodeResetFormState.value.email)

        _requestCodeResetFormState.value = _requestCodeResetFormState.value.copy(
            usernameError = usernameError,
            emailError = emailError,
            isValid = usernameError == null && emailError == null
        )

        if (_requestCodeResetFormState.value.isValid) {
            val requestCodeResetRequest = RequestCodeResetRequest(
                username = _requestCodeResetFormState.value.username,
                email = _requestCodeResetFormState.value.email
            )
            // actual api call
            requestCodeResetCode(requestCodeResetRequest)
        }
    }

    fun onCodeResetSubmitHandler() {
        val newCodeError = validateCode(_codeResetFormState.value.newCode)
        val oldCodeError = validateCode(_codeResetFormState.value.oldCode)
        // Using the username validator for tokens as they require the same validation requirements
        val resetTokenError = validateUsername(_codeResetFormState.value.resetToken)

        _codeResetFormState.value = _codeResetFormState.value.copy(
            newCodeError = newCodeError,
            oldCodeError = oldCodeError,
            resetTokenError = resetTokenError,
            isValid = newCodeError == null && oldCodeError == null && resetTokenError == null
        )

        if (_codeResetFormState.value.isValid) {
            val codeResetRequest = CodeResetRequest(
                oldCode = _codeResetFormState.value.oldCode.toInt(),
                newCode = _codeResetFormState.value.newCode.toInt(),
                resetToken = _codeResetFormState.value.resetToken
            )
            // actual api call
            codeResetService(codeResetRequest)
        }
    }

    private fun requestCodeResetCode(requestCodeResetRequest: RequestCodeResetRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            codeResetRepository.requestCodeResetCode(requestCodeResetRequest)
                .collectLatest { requestCodeRes ->
                    when (requestCodeRes) {
                        is ResourceState.Pending -> {
                            _requestCodeResetQueryState.value =
                                _requestCodeResetQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _requestCodeResetQueryState.value =
                                _requestCodeResetQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _requestCodeResetQueryState.value =
                                _requestCodeResetQueryState.value.copy(
                                    isLoading = false,
                                    data = requestCodeRes.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _requestCodeResetQueryState.value =
                                _requestCodeResetQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = requestCodeRes.error.toString()
                                )
                        }
                    }
                }
        }
    }

    private fun codeResetService(codeResetRequest: CodeResetRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            codeResetRepository.codeResetService(codeResetRequest)
                .collectLatest { codeResetRes ->
                    when (codeResetRes) {
                        is ResourceState.Pending -> {
                            _codeResetQueryState.value =
                                _codeResetQueryState.value.copy(
                                    isLoading = false,
                                    data = null,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Loading -> {
                            _codeResetQueryState.value =
                                _codeResetQueryState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResourceState.Success -> {
                            _codeResetQueryState.value =
                                _codeResetQueryState.value.copy(
                                    isLoading = false,
                                    data = codeResetRes.data,
                                    errorMsg = ""
                                )
                        }

                        is ResourceState.Error -> {
                            _codeResetQueryState.value =
                                _codeResetQueryState.value.copy(
                                    isLoading = false,
                                    errorMsg = codeResetRes.error.toString()
                                )
                        }
                    }
                }
        }
    }
}