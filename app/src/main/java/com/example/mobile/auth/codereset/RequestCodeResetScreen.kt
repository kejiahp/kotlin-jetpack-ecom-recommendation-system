package com.example.mobile.auth.codereset

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.CustomText
import com.example.mobile.core.PasswordTextField
import com.example.mobile.core.StyledOutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.mobile.core.BackButtonWithTitle

@Composable
fun RequestCondeResetScreen() {
    var username by remember {mutableStateOf("")}

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.fillMaxWidth()) {
                BackButtonWithTitle("Request Reset Token") { println("Reset Code Back Button Clicked") }

                CustomText(
                    text = "Enter your username and email address, if the provided username exists in the system code reset token will be sent to the provided email address.",
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

                StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = username,
                    imgVec = Icons.Default.Email,
                    onChange = { it -> username = it },
                    label = { CustomText("Email") },
                    placeholder = { CustomText("Enter your Email") },
                    emptyFieldHandler = {
                        username = ""
                    })
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { println("Send code clicked") }) {
                    CustomText("Send code", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(name="request code reset screen", showBackground = true)
@Composable
fun RequestCondeResetScreenPreview() {
    RequestCondeResetScreen()
}