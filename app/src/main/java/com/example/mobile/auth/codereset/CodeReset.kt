package com.example.mobile.auth.codereset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CustomText
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.R
import com.example.mobile.core.BackButton
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.PasswordTextField

@Composable
fun CodeReset(navController: NavController = rememberNavController()) {
    var username by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

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
                BackButtonWithTitle("Reset Code", onBackClick = { navController.popBackStack() })

                CustomText(
                    text = "Enter the reset token sent to your email address, your old code and your new code",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                StyledOutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = username,
                    imgVec = ImageVector.vectorResource(R.drawable.key_icon),
                    onChange = { it -> username = it },
                    label = { CustomText("Reset Token") },
                    placeholder = { CustomText("Enter your Reset Token") },
                    emptyFieldHandler = {
                        username = ""
                    })

                Spacer(modifier = Modifier.height(10.dp))

                PasswordTextField(
                    labelText = "Old Code",
                    placeholderText = "Enter your Old Code",
                    password = username,
                    onChangePassword = { username = it })

                Spacer(modifier = Modifier.height(10.dp))

                PasswordTextField(
                    labelText = "New Code",
                    placeholderText = "Enter your New Code",
                    password = username,
                    onChangePassword = { username = it })
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { println("Change code clicked") }) {
                    CustomText("Change code", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}


@Preview(name = "code reset", showBackground = true)
@Composable
fun CodeResetPreview() {
    CodeReset()
}