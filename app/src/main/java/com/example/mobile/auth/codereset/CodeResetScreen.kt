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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CustomText
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.R
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.PasswordTextField
import com.example.mobile.core.auth.AuthViewModel
import com.example.mobile.core.auth.RedirectToHomeScreen
import com.example.mobile.core.navigation.NavRoutes

@Composable
fun CodeResetScreen(
    navController: NavController = rememberNavController(),
    codeResetViewModel: CodeResetViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val toastCtx = LocalContext.current
    val codeResetFormState by codeResetViewModel.codeResetFormState.collectAsState()
    val codeResetQueryState by codeResetViewModel.codeResetQueryState.collectAsState()

    RedirectToHomeScreen(navController) {
        LaunchedEffect(codeResetQueryState) {
            // Show error message on code reset fail
            if (codeResetQueryState.errorMsg.isNotEmpty()) {
                Toast.makeText(
                    toastCtx, codeResetQueryState.errorMsg, Toast.LENGTH_LONG
                ).show()
            }
            // show message on successful code reset
            if (codeResetQueryState.data != null) {
                Toast.makeText(toastCtx, codeResetQueryState.data!!.message, Toast.LENGTH_LONG)
                    .show()

                //sign the current active user out
                authViewModel.deleteAuthUser()

                // navigate to the login screen
                navController.navigate(NavRoutes.Login)
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
                        "Reset Code",
                        onBackClick = { navController.popBackStack() })

                    CustomText(
                        text = "Enter the reset token sent to your email address, your old code and your new code",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        textFieldError = codeResetFormState.resetTokenError,
                        value = codeResetFormState.resetToken,
                        imgVec = ImageVector.vectorResource(R.drawable.key_icon),
                        onChange = codeResetViewModel::onCodeResetTokenChangeHandler,
                        label = { CustomText("Reset Token") },
                        placeholder = { CustomText("Enter your Reset Token") },
                        emptyFieldHandler = {
                            codeResetViewModel.onCodeResetTokenChangeHandler("")
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordTextField(
                        textFieldError = codeResetFormState.oldCodeError,
                        labelText = "Old Code",
                        placeholderText = "Enter your Old Code",
                        password = codeResetFormState.oldCode,
                        onChangePassword = codeResetViewModel::onCodeResetOldCodeChangeHandler
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordTextField(
                        labelText = "New Code",
                        placeholderText = "Enter your New Code",
                        textFieldError = codeResetFormState.newCodeError,
                        password = codeResetFormState.newCode,
                        onChangePassword = codeResetViewModel::onCodeResetNewCodeChangeHandler
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (codeResetQueryState.errorMsg.isNotEmpty()) {
                        CustomText(codeResetQueryState.errorMsg, color = Color.Red)
                    }

                    LoaderButton(
                        isLoading = codeResetQueryState.isLoading,
                        onClickBtn = codeResetViewModel::onCodeResetSubmitHandler
                    ) {
                        CustomText("Change code", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

    }
}

