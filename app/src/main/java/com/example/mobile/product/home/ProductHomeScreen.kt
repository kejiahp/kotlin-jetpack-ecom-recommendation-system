package com.example.mobile.product.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomText
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.core.auth.AuthViewModel

@Composable
fun ProductHomeScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var searchValue by remember { mutableStateOf("") }
    val authUser by authViewModel.authUser.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            CustomText("Hi ${authUser?.username ?: ""}", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            CustomText("What would you like to buy today?")

            Spacer(modifier = Modifier.height(10.dp))

            StyledOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                textFieldError = null,
                value = searchValue,
                imgVec = Icons.Default.Search,
                onChange = { it -> searchValue = it },
                label = { CustomText("Search for an Item") },
                placeholder = { CustomText("Search for an Item") },
                emptyFieldHandler = {
                    searchValue = ""
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomText("Categories", fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(5.dp))

            LazyRow {
                items(List(20) { "" }) { item ->
                    CustomButton(
                        onClickBtn = {},
                        modifier = Modifier.padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = MaterialTheme.shapes.extraLarge
                    ) { CustomText("Vegetable") }
                }
            }

        }
    }
}

@Preview(name = "Product home screen preview", showBackground = true)
@Composable
fun ProductHomeScreenPreview() {
    ProductHomeScreen()
}