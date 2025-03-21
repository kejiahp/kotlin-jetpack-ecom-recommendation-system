package com.example.mobile.auth.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.CustomText
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CoreConstants
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomExposedDropdownMenu
import com.example.mobile.core.Loader
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.core.utilites.ResourceState

const val TAG = "SignUpScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController = rememberNavController(),
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val countryList = CoreConstants.countryList
    val genderList = CoreConstants.genderList
    val toastCtx = LocalContext.current
    var locationExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    val resData by signUpViewModel.resData.collectAsState()
    val formState by signUpViewModel.formState.collectAsState()

    formState.gender.let {
        LaunchedEffect(it) {
            if (it.isEmpty()) {
                signUpViewModel.onGenderChangeHandler(genderList[0])
            }
        }
    }
    formState.location.let {
        LaunchedEffect(it) {
            if (it.isEmpty()) {
                signUpViewModel.onLocationChangeHandler(countryList[0])
            }
        }
    }


    when(resData) {
        is ResourceState.Loading -> {
            Log.d(TAG, "Loading")
        }
        is ResourceState.Success -> {
            Log.d(TAG, "Success")
            CoreUtils.printDebugger(TAG, (resData as ResourceState.Success<RegisterResponse>).data)
        }
        is ResourceState.Error -> {
            Log.d(TAG, "Error")
            Toast.makeText(
                toastCtx,
                (resData as ResourceState.Error<RegisterResponse>).error.toString(),
                Toast.LENGTH_LONG
            ).show()
            CoreUtils.printDebugger(TAG, (resData as ResourceState.Error<RegisterResponse>).error.toString())
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomText(text = "Sign Up", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(5.dp))
                CustomText(text = "Create an account", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(10.dp))
                // Username Input Field
                StyledOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.username,
                    textFieldError = formState.usernameError,
                    imgVec = Icons.Default.Person,
                    onChange = signUpViewModel::onUsernameChangeHandler,
                    label = { CustomText("Username") },
                    placeholder = { CustomText("Enter your Username") },
                    emptyFieldHandler = {
                        signUpViewModel.onUsernameChangeHandler("")
                    })

                Spacer(modifier = Modifier.height(10.dp))

                // Email Input Field
                StyledOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.email,
                    textFieldError = formState.emailError,
                    imgVec = Icons.Default.Email,
                    keyboardOpt = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onChange = signUpViewModel::onEmailChangeHandler,
                    label = { CustomText("Email") },
                    placeholder = { CustomText("Enter your Email") },
                    emptyFieldHandler = {
                        signUpViewModel.onEmailChangeHandler("")
                    })

                Spacer(modifier = Modifier.height(10.dp))

                // Age Input Field
                StyledOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.age.toString(),
                    textFieldError = formState.ageError,
                    imgVec = Icons.Outlined.Person,
                    keyboardOpt = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onChange = { it ->
                        if (it.isNotEmpty() && it.all { it.isDigit() }) {
                            signUpViewModel.onAgeChangeHandler(it.toInt())
                        }
                    },
                    label = { CustomText("Age") },
                    placeholder = { CustomText("Enter your Age") },
                    emptyFieldHandler = {
                        signUpViewModel.onAgeChangeHandler(18)
                    })

                Spacer(modifier = Modifier.height(10.dp))

                // Location Input Dropdown
                CustomExposedDropdownMenu(
                    selectedText = formState.location,
                    textFieldError = formState.locationError,
                    onSelectedText = signUpViewModel::onLocationChangeHandler,
                    expanded = locationExpanded,
                    onExpandedChange = { locationExpanded = it },
                    options = countryList,
                    label = { CustomText("Location") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Gender Input Dropdown
                CustomExposedDropdownMenu(
                    selectedText = formState.gender,
                    textFieldError = formState.genderError,
                    onSelectedText = signUpViewModel::onGenderChangeHandler,
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it },
                    options = genderList,
                    label = { CustomText("Gender") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomText("To ensure full anonymity, use the button below to fill the form with random user data", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(10.dp))
                CustomButton (onClickBtn = signUpViewModel::randomUserData) {
                    CustomText("Use Random")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoaderButton(
                    isLoading = false,
                    onClickBtn = {signUpViewModel.onSubmitHandler()}
                ) {
                    CustomText("Sign Up", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                TextButton(onClick = { println("Already have an account clicked") }) { CustomText("Already have an account?") }
            }
        }
    }
}


@Preview(name = "signup screen", showBackground = true)
@Composable
fun SignUpScreenPreview() {
    LoaderButton(
        isLoading = true,
        enabled = true,
        onClickBtn = {}
    ) {
        CustomText("Sign Up", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}