package com.example.mobile.auth.codereset

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.CustomText
import com.example.mobile.core.StyledOutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.auth.RedirectToHomeScreen
import com.example.mobile.core.navigation.NavRoutes

@Composable
fun RequestCondeResetScreen(
    navController: NavController = rememberNavController(),
    codeResetViewModel: CodeResetViewModel = hiltViewModel()
) {
    val toastCtx = LocalContext.current
    val scrollState = rememberScrollState()
    val requestCodeResetFormState by codeResetViewModel.requestCodeResetFormState.collectAsState()
    val requestCodeResetQueryState by codeResetViewModel.requestCodeResetQueryState.collectAsState()

    RedirectToHomeScreen(navController) {
        LaunchedEffect(requestCodeResetQueryState) {
            // Show error message on request for reset code fail
            if (requestCodeResetQueryState.errorMsg.isNotEmpty()) {
                Toast.makeText(
                    toastCtx, requestCodeResetQueryState.errorMsg, Toast.LENGTH_LONG
                ).show()
            }
            // show message on request for reset code success
            if (requestCodeResetQueryState.data != null) {
                Toast.makeText(
                    toastCtx,
                    requestCodeResetQueryState.data!!.message,
                    Toast.LENGTH_LONG
                )
                    .show()
                navController.navigate(NavRoutes.CodeResetScreen)
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
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BackButtonWithTitle(
                        "Request Reset Token",
                        onBackClick = { navController.popBackStack() })

                    CustomText(
                        text = "Enter your username and email address, if the provided username exists in the system code reset token will be sent to the provided email address.",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        textFieldError = requestCodeResetFormState.usernameError,
                        value = requestCodeResetFormState.username,
                        imgVec = Icons.Default.Person,
                        onChange = codeResetViewModel::onRequestCodeUsernameChangeHandler,
                        label = { CustomText("Username") },
                        placeholder = { CustomText("Enter your Username") },
                        emptyFieldHandler = {
                            codeResetViewModel.onRequestCodeUsernameChangeHandler("")
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        textFieldError = requestCodeResetFormState.emailError,
                        value = requestCodeResetFormState.email,
                        imgVec = Icons.Default.Email,
                        onChange = codeResetViewModel::onRequestCodeEmailChangeHandler,
                        label = { CustomText("Email") },
                        placeholder = { CustomText("Enter your Email") },
                        emptyFieldHandler = {
                            codeResetViewModel.onRequestCodeEmailChangeHandler("")
                        })
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (requestCodeResetQueryState.errorMsg.isNotEmpty()) {
                        CustomText(requestCodeResetQueryState.errorMsg, color = Color.Red)
                    }

                    LoaderButton(
                        isLoading = requestCodeResetQueryState.isLoading,
                        onClickBtn = codeResetViewModel::onRequestCodeSubmitHandler
                    ) {
                        CustomText("Send code", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
