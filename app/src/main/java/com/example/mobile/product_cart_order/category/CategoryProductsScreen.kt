package com.example.mobile.product_cart_order.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.ErrorScreen
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.StickyTopNavbar
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.product_cart_order.components.ProductGrid
import com.example.mobile.product_cart_order.preference.ProdCartOrderSharedPreferenceViewModel

@Composable
fun CategoryProductsScreen(
    categoryId: String,
    navController: NavController,
    categoryProductsViewModel: CategoryProductsViewModel = hiltViewModel(),
    prodCartOrderSharedPreferenceViewModel: ProdCartOrderSharedPreferenceViewModel = hiltViewModel()
) {
    val allCategoryProductQueryState by categoryProductsViewModel.allCategoryProductQueryState.collectAsState()

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
                            StickyTopNavbar(
                                navController = navController,
                            ) {
                                BackButtonWithTitle(
                                    title = allCategories.data.name,
                                    titleModifier = Modifier.fillMaxWidth(),
                                    maxLines = 1,
                                    titleOverflow = TextOverflow.Ellipsis
                                ) { navController.popBackStack() }
                            }
                        },
                        onProductClick = { it ->
                            prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)
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
