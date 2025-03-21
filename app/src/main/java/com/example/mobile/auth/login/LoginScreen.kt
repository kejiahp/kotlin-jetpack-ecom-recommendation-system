package com.example.mobile.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile.core.CustomText
import com.example.mobile.core.PasswordTextField
import com.example.mobile.core.StyledOutlinedTextField

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomText(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(5.dp))
                CustomText(
                    text = "Enter your username and code, sent to your email address to continue",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = username,
                    imgVec = Icons.Default.Person,
                    onChange = { it -> username = it },
                    label = { CustomText("Username") },
                    placeholder = { CustomText("Enter your Username") },
                    emptyFieldHandler = {
                        username = ""
                    })

                Spacer(modifier = Modifier.height(10.dp))

                PasswordTextField(labelText="Code", placeholderText="Enter your Code", password = username, onChangePassword = { username = it })
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { println("Login clicked") }) {
                    CustomText("Login", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                TextButton(onClick = { println("Don't have an account sign up clicked") }) { CustomText("Don't have an account?") }
                TextButton(onClick = { println("Forgot your clicked") }) { CustomText("Forgot your code?") }
            }
        }
    }
}


@Preview(name = "Login screen", showBackground = true)
@Composable
fun LoginScreenPreview() {
//    LoginScreen()
}