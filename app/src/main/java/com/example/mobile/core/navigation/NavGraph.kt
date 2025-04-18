package com.example.mobile.core.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobile.auth.codereset.CodeResetScreen
import com.example.mobile.auth.codereset.RequestCondeResetScreen
import com.example.mobile.auth.login.LoginScreen
import com.example.mobile.auth.signup.SignUpScreen
import com.example.mobile.product_cart_order.cart.CartScreen
import com.example.mobile.product_cart_order.category.CategoryProductsScreen
import com.example.mobile.product_cart_order.details.ProductDetailsScreen
import com.example.mobile.product_cart_order.home.ProductHomeScreen
import com.example.mobile.product_cart_order.orders.OrderScreen

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

    @Serializable
    object CartScreen

    @Serializable
    object OrderScreen

    @Serializable
    data class ProductDetails(val productId: String)

    @Serializable
    data class CategoryProducts(val categoryId: String)
}

/**
 * Nav graph mapping the various screen to various navigation routes
 * */
@Composable
fun NavGraph(
    modifier: Modifier,
    navController: NavHostController,
) {
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
        composable<NavRoutes.ProductDetails> { backStackEntry ->
            val productDetail: NavRoutes.ProductDetails = backStackEntry.toRoute()
            ProductDetailsScreen(productId = productDetail.productId, navController = navController)
        }
        composable<NavRoutes.CategoryProducts> { backStackEntry ->
            val categoryProducts: NavRoutes.CategoryProducts = backStackEntry.toRoute()
            CategoryProductsScreen(
                categoryId = categoryProducts.categoryId,
                navController = navController
            )
        }
        composable<NavRoutes.CartScreen> {
            CartScreen(navController = navController)
        }
        composable<NavRoutes.OrderScreen> {
            OrderScreen(navController = navController)
        }
    }
}