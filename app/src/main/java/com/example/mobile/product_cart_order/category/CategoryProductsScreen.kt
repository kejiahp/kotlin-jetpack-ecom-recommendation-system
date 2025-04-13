package com.example.mobile.product_cart_order.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.ErrorScreen
import com.example.mobile.core.Loader
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.product_cart_order.components.ProductGrid
import com.example.mobile.product_cart_order.components.ProductListing
import com.example.mobile.product_cart_order.home.ProductHomeViewModel

@Composable
fun CategoryProductsScreen(
    categoryId: String,
    navController: NavController,
    productHomeViewModel: ProductHomeViewModel = hiltViewModel(),
    categoryProductsViewModel: CategoryProductsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val allCategoryProductQueryState by categoryProductsViewModel.allCategoryProductQueryState.collectAsState()

    CoreUtils.printDebugger("CategoryProductsScreen", allCategoryProductQueryState)

    LaunchedEffect(categoryId) {
        if (categoryId.isEmpty()) {
            navController.navigate(NavRoutes.ProductHomeScreen)
        } else {
            categoryProductsViewModel.getAllProductsInCategory(categoryId = categoryId)
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            allCategoryProductQueryState.isLoading -> LoaderScreen()

            allCategoryProductQueryState.errorMsg.isNotEmpty() -> ErrorScreen(
                allCategoryProductQueryState.errorMsg
            )

            allCategoryProductQueryState.data != null -> allCategoryProductQueryState.data?.let { allCategories ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    ProductGrid(
                        title = allCategories.data.name,
                        products = allCategories.data.products,
                        headerContent = {
                            BackButtonWithTitle(
                                title = allCategories.data.name,
                                titleModifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                titleOverflow = TextOverflow.Ellipsis
                            ) { navController.popBackStack() }
                        },
                        onProductClick = { it ->
                            productHomeViewModel.addToRecentlyViewed(it.id)
                            navController.navigate(
                                NavRoutes.ProductDetails(
                                    productId = it.id
                                )
                            )
                        }
                    )
                }

            }
        }

    }
}
