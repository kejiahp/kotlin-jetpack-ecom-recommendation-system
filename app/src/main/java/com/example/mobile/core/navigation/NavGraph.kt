package com.example.mobile.core.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mobile.auth.login.LoginScreen
import com.example.mobile.auth.signup.SignUpScreen

import kotlinx.serialization.Serializable


/**
 * object declaration, storing containing all navigation route keys
 */
object NavRoutes {
    @Serializable
    object Login

    @Serializable
    object SignUp
}

/**
 * Nav graph mapping the various screen to various navigation routes
 * */
@Composable
fun NavGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoutes.SignUp
    ) {
        composable<NavRoutes.SignUp> {
            SignUpScreen(navController)
        }
        composable<NavRoutes.Login> {
            LoginScreen(navController)
        }

    }
}