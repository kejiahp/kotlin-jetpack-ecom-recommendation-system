package com.example.mobile.auth.signup

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.core.utilites.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpFormState(
    val username: String = "",
    val usernameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val age: Int = 18,
    val ageError: String? = null,
    val location: String = "",
    val locationError: String? = null,
    val gender: String = "",
    val genderError: String? = null,
    val isValid: Boolean = false
)


/**
 * The `@HiltViewModel` annotation ensures the view model is accessible via hilt
 *
 * The `@Inject` and an empty constructor are essential for hilt to inject the viewModel, where it is needed, at runtime
 * */
@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpRepository: SignUpRepository) :
    ViewModel() {
    init {
//        Log.d(TAG, "Signup view model initialized")
//        getData(name = "nathaniel")
    }

    /** User data state */
    private val _formState: MutableStateFlow<SignUpFormState> = MutableStateFlow(
        SignUpFormState()
    )
    val formState = _formState.asStateFlow()

    /** Register/SignUp API call response state state */
    private val _resData: MutableStateFlow<ResourceState<RegisterResponse>> = MutableStateFlow(
        ResourceState.Loading()
    )
    val resData = _resData.asStateFlow()

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

    /**
     * Validate age
     *
     * Checking if it is less than 18 or greater than 100
     * */
    private fun validateAge(age: Int): String? {
        return if (_formState.value.age < 18 || _formState.value.age > 100) "The minimum age is 18 and the maximum age is 100"
        else null

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
     * Validate form state
     * */
    fun onSubmitHandler() {
        // Run execute the email, age and username validators
        val emailError = validateEmail(_formState.value.email)
        val ageError = validateAge(_formState.value.age)
        val usernameError = validateUsername(_formState.value.username)

        // update the form state with error values
        _formState.value = _formState.value.copy(
            emailError = emailError,
            ageError = ageError,
            usernameError = usernameError,
            isValid = emailError == null && ageError == null && usernameError == null
        )
        // If the form state is valid, initiate register request
        if (_formState.value.isValid) {
            val registerReq: RegisterRequest = RegisterRequest(
                username = _formState.value.username,
                email = _formState.value.email,
                age = _formState.value.age,
                location = _formState.value.location,
                gender = _formState.value.gender
            )
            CoreUtils.printDebugger(TAG, registerReq)
            registerUser(registerReq)
        }
    }

    /**Sends a post request containing users data in the body to the server*/
    private fun registerUser(userData: RegisterRequest) {
        // `viewModelScope.launch` builds a co-routine without blocking the current thread
        // `Dispatchers.IO` specifies the co-routine should be running on the `Dispatchers.IO` dispatcher in background thread
        viewModelScope.launch(Dispatchers.IO) {
            signUpRepository.registerUser(userData)
                .collectLatest { signupRes -> _resData.value = signupRes }
        }
    }

    /**
     * Fill the form with random user data
     * */
    fun randomUserData() {
        _formState.value =  _formState.value.copy(
            email="popoolakejiah@gmail.com",
            age = 18,
            username = "kejiah",
            isValid = true
        )
    }

    /** controls changes in username, ensures username is only in lowercase and spaces are automatically converted to underscores */
    fun onUsernameChangeHandler(text: String) {
        _formState.update { oldState ->
            oldState.copy(
                username = text.lowercase().replace(" ", "_")
            )
        }
    }

    /** controls changes in email */
    fun onEmailChangeHandler(text: String) {
        _formState.update { oldState -> oldState.copy(email = text) }
    }

    /** controls changes in age */
    fun onAgeChangeHandler(age: Int) {
        _formState.update { oldState -> oldState.copy(age = age) }
    }

    /** controls changes in location */
    fun onLocationChangeHandler(location: String) {
        _formState.update { oldState -> oldState.copy(location = location) }
    }

    /** controls changes in gender */
    fun onGenderChangeHandler(gender: String) {
        _formState.update { oldState -> oldState.copy(gender = gender) }
    }

    companion object {
        const val TAG = "SignUpViewModel"
    }
}
