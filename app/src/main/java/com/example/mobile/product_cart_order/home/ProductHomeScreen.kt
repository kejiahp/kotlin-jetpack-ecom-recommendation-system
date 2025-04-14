package com.example.mobile.product_cart_order.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomText
import com.example.mobile.core.LoaderRow
import com.example.mobile.core.SearchableDropdown
import com.example.mobile.core.StickyTopNavbar
import com.example.mobile.core.auth.AuthViewModel
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.product_cart_order.components.ProductListing
import com.example.mobile.product_cart_order.entity.ProductListingDetailsData
import com.example.mobile.product_cart_order.preference.ProdCartOrderSharedPreferenceViewModel

@Composable
fun ProductHomeScreen(
    navController: NavController = rememberNavController(),
    productHomeViewModel: ProductHomeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    prodCartOrderSharedPreferenceViewModel: ProdCartOrderSharedPreferenceViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val authUser by authViewModel.authUser.collectAsState()

    val productListingQueryState by productHomeViewModel.productListingQueryState.collectAsState()
    val allCategoryQueryState by productHomeViewModel.allCategories.collectAsState()

    val productSearchResults by productHomeViewModel.productSearchResults.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            StickyTopNavbar(
                navController = navController,
            ) {
                CustomText(
                    "Hi ${authUser?.username ?: ""}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                CustomText("What would you like to buy today?")
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .weight(1f)

            ) {
                Spacer(modifier = Modifier.height(5.dp))

                var selectedFruit by remember { mutableStateOf<ProductListingDetailsData?>(null) }

                val searchProductList =
                    if (productSearchResults.data != null) productSearchResults.data!!.data else listOf()

                // Dropdown of searchable products
                SearchableDropdown<ProductListingDetailsData>(
                    items = if (searchProductList.size >= 5) searchProductList.subList(
                        0,
                        5
                    ) else searchProductList.subList(0, searchProductList.size),
                    selectedItem = selectedFruit,
                    onItemSelected = {
                        selectedFruit = it
                        prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)
                        navController.navigate(NavRoutes.ProductDetails(productId = it.id))
                    },
                    onChangeHandler = { productHomeViewModel.searchProductByName(it) },
                    itemLabel = { if (it != null) it.productName else "" },
                    itemContent = { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .background(Color.White),
                        ) {
                            // Product image
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.productName,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.placeholder_image),
                                error = painterResource(id = R.drawable.placeholder_image),
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                CustomText(item.productName, fontSize = 10.sp)
                                CustomText(
                                    "₦ ${item.sellingPrice}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // RENDER CATEGORIES
                when {
                    allCategoryQueryState.isLoading -> LoaderRow()

                    allCategoryQueryState.errorMsg.isNotEmpty() -> CustomText("Failed to get all categories: ${allCategoryQueryState.errorMsg}")

                    allCategoryQueryState.data != null -> {
                        allCategoryQueryState.data?.let { catData ->
                            CustomText(
                                "Categories",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )

                            LazyRow {
                                items(catData.data.categories) { item ->
                                    CustomButton(
                                        onClickBtn = {
                                            navController.navigate(
                                                NavRoutes.CategoryProducts(
                                                    categoryId = item.id
                                                )
                                            )
                                        },
                                        modifier = Modifier.padding(end = 10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                        shape = MaterialTheme.shapes.extraLarge
                                    ) { CustomText(item.name) }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height((5.dp)))

                // RENDER PRODUCT LISTING
                when {
                    productListingQueryState.isLoading -> LoaderRow()

                    productListingQueryState.errorMsg.isNotEmpty() -> CustomText("Failed to get all categories: ${allCategoryQueryState.errorMsg}")

                    productListingQueryState.data != null -> {
                        productListingQueryState.data?.let { productData ->

                            ProductListing(
                                title = "Explore",
                                products = productData.data.explore,
                                onProductClick = { it ->
                                    prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                    navController.navigate(
                                        NavRoutes.ProductDetails(
                                            productId = it.id
                                        )
                                    )
                                }
                            )

                            ProductListing(
                                title = "Trending",
                                products = productData.data.trending,
                                onProductClick = { it ->
                                    prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                    navController.navigate(
                                        NavRoutes.ProductDetails(
                                            productId = it.id
                                        )
                                    )
                                }
                            )

                            ProductListing(
                                title = "New Added",
                                products = productData.data.newAdded,
                                onProductClick = { it ->
                                    prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                    navController.navigate(
                                        NavRoutes.ProductDetails(
                                            productId = it.id
                                        )
                                    )
                                }
                            )

                            ProductListing(
                                title = "Similar To Recently Viewed",
                                products = productData.data.similarToRecentView,
                                onProductClick = { it ->
                                    prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                    navController.navigate(
                                        NavRoutes.ProductDetails(
                                            productId = it.id
                                        )
                                    )
                                }
                            )

                            authUser?.let {
                                ProductListing(
                                    title = "Might Interest You",
                                    products = productData.data.mightInterestYou,
                                    onProductClick = { it ->
                                        prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                        navController.navigate(
                                            NavRoutes.ProductDetails(
                                                productId = it.id
                                            )
                                        )
                                    }
                                )

                                ProductListing(
                                    title = "Based On Your Location",
                                    products = productData.data.sameLocation,
                                    onProductClick = { it ->
                                        prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                        navController.navigate(
                                            NavRoutes.ProductDetails(
                                                productId = it.id
                                            )
                                        )
                                    }
                                )

                                ProductListing(
                                    title = "Based On Your Age Range",
                                    products = productData.data.ageRange,
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
        }
    }
}

@Preview(name = "Product home screen preview", showBackground = true)
@Composable
fun ProductHomeScreenPreview() {

}