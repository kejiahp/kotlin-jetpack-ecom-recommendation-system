package com.example.mobile.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CustomText
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.PasswordTextField
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.core.auth.AuthViewModel
import com.example.mobile.core.auth.RedirectToHomeScreen
import com.example.mobile.core.navigation.NavRoutes


@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val toastCtx = LocalContext.current
    val resData by loginViewModel.resData.collectAsState()
    val formState by loginViewModel.formState.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val errorMsg by loginViewModel.errorMsg.collectAsState()

    RedirectToHomeScreen(navController) {
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
                if (it.success) {
                    // update authenticated user data
                    authViewModel.updateAuthUser(it.data)

                    // navigate to product listing screen
                    navController.navigate(NavRoutes.ProductHomeScreen)
                }
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomText(
                            text = "Login",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        IconButton(onClick = { navController.navigate(NavRoutes.ProductHomeScreen) }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    CustomText(
                        text = "Enter your username and code, sent to your email address to continue",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        textFieldError = formState.usernameError,
                        value = formState.username,
                        imgVec = Icons.Default.Person,
                        onChange = loginViewModel::onUsernameChangeHandler,
                        label = { CustomText("Username") },
                        placeholder = { CustomText("Enter your Username") },
                        emptyFieldHandler = {
                            loginViewModel.onUsernameChangeHandler("")
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordTextField(
                        textFieldError = formState.codeError,
                        labelText = "Code",
                        placeholderText = "Enter your Code",
                        password = formState.code,
                        onChangePassword = loginViewModel::onCodeChangeHandler
                    )
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
                        onClickBtn = { loginViewModel.onLoginHandler() }
                    ) {
                        CustomText("Login", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    TextButton(onClick = { navController.navigate(NavRoutes.SignUp) }) {
                        CustomText(
                            "Don't have an account?"
                        )
                    }
                    TextButton(onClick = { navController.navigate(NavRoutes.RequestCondeResetScreen) }) {
                        CustomText(
                            "Forgot your code?"
                        )
                    }
                }
            }
        }
    }
}

