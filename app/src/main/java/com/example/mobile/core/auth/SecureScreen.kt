package com.example.mobile.core.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mobile.core.navigation.NavRoutes

@Composable
fun SecureScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val authUser by authViewModel.authUser.collectAsState()

    LaunchedEffect(authUser) {
        if (authUser == null) {
            navController.navigate(NavRoutes.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (authUser != null) {
        content()
    }
}