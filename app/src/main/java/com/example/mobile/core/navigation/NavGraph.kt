package com.example.mobile.core.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mobile.auth.codereset.CodeResetScreen
import com.example.mobile.auth.codereset.RequestCondeResetScreen
import com.example.mobile.auth.login.LoginScreen
import com.example.mobile.auth.signup.SignUpScreen
import com.example.mobile.product.home.ProductHomeScreen

import kotlinx.serialization.Serializable


/**
 * object declaration, storing containing all navigation route keys
 */
object NavRoutes {
    @Serializable
    object Login

    @Serializable
    object SignUp

    @Serializable
    object RequestCondeResetScreen

    @Serializable
    object CodeResetScreen

    @Serializable
    object ProductHomeScreen
}

/**
 * Nav graph mapping the various screen to various navigation routes
 * */
@Composable
fun NavGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoutes.ProductHomeScreen
    ) {
        composable<NavRoutes.SignUp> {
            SignUpScreen(navController)
        }
        composable<NavRoutes.Login> {
            LoginScreen(navController)
        }
        composable<NavRoutes.CodeResetScreen> {
            CodeResetScreen(navController)
        }
        composable<NavRoutes.RequestCondeResetScreen> {
            RequestCondeResetScreen(navController)
        }
        composable<NavRoutes.ProductHomeScreen> {
            ProductHomeScreen(navController)
        }
    }
}