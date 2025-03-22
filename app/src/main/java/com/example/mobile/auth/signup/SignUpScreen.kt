package com.example.mobile.auth.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.CustomText
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CoreConstants
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomExposedDropdownMenu
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.core.navigation.NavRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val countryList = CoreConstants.countryList
    val genderList = CoreConstants.genderList
    val toastCtx = LocalContext.current
    var locationExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val resData by signUpViewModel.resData.collectAsState()
    val formState by signUpViewModel.formState.collectAsState()
    val errorMsg by signUpViewModel.errorMsg.collectAsState()
    val isLoading by signUpViewModel.isLoading.collectAsState()

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

    // Show error message on signup fail
    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx,
                errorMsg,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Show message on signup success
    resData?.let {
        LaunchedEffect(it) {
            if (it.message.isNotEmpty()) {
                Toast.makeText(
                    toastCtx,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(10.dp)
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

                CustomText(
                    "To ensure full anonymity, use the button below to fill the form with random user data. Emails provided are also not stored in the system, they are only used to send your authentication details.",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                CustomButton(onClickBtn = signUpViewModel::randomUserData) {
                    CustomText("Use Random")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (errorMsg.isNotEmpty()) {
                    CustomText(errorMsg, color = Color.Red)
                }
                LoaderButton(
                    isLoading = isLoading,
                    onClickBtn = { signUpViewModel.onSubmitHandler() }
                ) {
                    CustomText("Sign Up", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                TextButton(onClick = { navController.navigate(NavRoutes.Login) }) { CustomText("Already have an account?") }
            }
        }
    }
}