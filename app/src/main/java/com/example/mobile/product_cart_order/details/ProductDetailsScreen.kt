package com.example.mobile.product_cart_order.details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.core.BackButton
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomText
import com.example.mobile.core.ErrorScreen
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.LoaderRow
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.StickyTopNavbar
import com.example.mobile.core.auth.AuthViewModel
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.product_cart_order.components.ProductListing
import com.example.mobile.product_cart_order.home.ProductHomeViewModel
import com.example.mobile.product_cart_order.preference.ProdCartOrderSharedPreferenceViewModel

@Composable
fun ProductDetailsScreen(
    productId: String,
    navController: NavController = rememberNavController(),
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    prodCartOrderSharedPreferenceViewModel: ProdCartOrderSharedPreferenceViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val toastCtx = LocalContext.current
    var quantity by remember { mutableIntStateOf(1) }
    val productDetailsQueryState by productDetailsViewModel.productDetailsQueryState.collectAsState()
    val relatedProductQueryState by productDetailsViewModel.relatedProductQueryState.collectAsState()
    val addToCartQueryState by productDetailsViewModel.addToCartQueryState.collectAsState()
    val authUser by authViewModel.authUser.collectAsState()

    fun onAddToCartClickHandler(prodId: String, qty: Int) {
        if(authUser != null) {
            productDetailsViewModel.addToCartHandler(
                prodId,
                qty,
                "ADD"
            )
        }else {
            Toast.makeText(toastCtx, "Login to add items to cart", Toast.LENGTH_LONG)
                .show()
            navController.navigate(NavRoutes.Login)
        }
    }



    LaunchedEffect(productId) {
        if (productId.isEmpty()) {
            navController.navigate(NavRoutes.ProductHomeScreen)
        } else {
            productDetailsViewModel.getProductDetails(productId = productId)
            productDetailsViewModel.getRelatedProducts(productId = productId)
        }
    }

    LaunchedEffect(addToCartQueryState) {
        if (addToCartQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, addToCartQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (addToCartQueryState.data != null) {
            Toast.makeText(toastCtx, addToCartQueryState.data!!.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            productDetailsQueryState.isLoading -> LoaderScreen()

            productDetailsQueryState.errorMsg.isNotEmpty() -> ErrorScreen(productDetailsQueryState.errorMsg)

            productDetailsQueryState.data != null -> {
                productDetailsQueryState.data?.let { productData ->

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                    ) {
                        StickyTopNavbar(
                            navController = navController,
                        ) {
                            BackButtonWithTitle(
                                title = productData.data.productName,
                                titleModifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                titleOverflow = TextOverflow.Ellipsis
                            ) { navController.popBackStack() }
                        }


                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .weight(1f),
                        ) {
                            AsyncImage(
                                model = productData.data.imageUrl,
                                contentDescription = productData.data.productName,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.placeholder_image),
                                error = painterResource(id = R.drawable.placeholder_image),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    CustomText(
                                        productData.data.productName,
                                        fontSize = 18.sp,
                                        modifier = Modifier.fillMaxWidth(0.8f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Row {
                                        CustomText(
                                            "Category: ${productData.data.categoryId.name}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Light,
                                        )
                                        VerticalDivider(
                                            thickness = 1.dp,
                                            modifier = Modifier
                                                .padding(horizontal = 5.dp)
                                                .height(14.dp)
                                        )
                                    }

                                    CustomText(
                                        "Location: ${productData.data.location}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Light,
                                    )

                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    CustomText(text = "${productData.data.avgRating}")
                                }
                            }

                            Row(verticalAlignment = Alignment.Bottom) {
                                CustomText(
                                    text = "₦ ${productData.data.sellingPrice}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                CustomText(
                                    text = "₦ ${productData.data.productPrice}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }

                            CustomText("${productData.data.productQuantity} Units available")

                            HorizontalDivider(
                                thickness = 2.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )

                            CustomText(
                                "Description",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            CustomText(productData.data.productDescription, fontSize = 14.sp)

                            HorizontalDivider(
                                thickness = 2.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )

                            CustomText("Ratings", fontSize = 18.sp, fontWeight = FontWeight.Medium)

                            Spacer(modifier = Modifier.height(5.dp))

                            if (productData.data.productRatings.isNotEmpty()) {
                                productData.data.productRatings.forEach { rating ->
                                    Column(modifier = Modifier.padding(vertical = 5.dp)) {
                                        CustomText(
                                            "Name: ${rating.userId.username} | Age: ${rating.userId.age} | Location: ${rating.userId.location}",
                                            fontSize = 14.sp
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            List(rating.rating) { "" }.forEach {
                                                Icon(
                                                    Icons.Default.Star,
                                                    contentDescription = "Rating",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier
                                                        .padding(end = 5.dp)
                                                        .size(16.dp)
                                                )

                                            }
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(
                                thickness = 2.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )

                            when {
                                relatedProductQueryState.isLoading -> LoaderRow()

                                relatedProductQueryState.errorMsg.isNotEmpty() -> CustomText("Failed to get all categories: ${relatedProductQueryState.errorMsg}")

                                relatedProductQueryState.data != null -> {
                                    relatedProductQueryState.data?.let { relatedProd ->
                                        ProductListing(
                                            title = "Related Products",
                                            products = relatedProd.data,
                                            onProductClick = {
                                                prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(it.id)

                                                navController.navigate(
                                                    NavRoutes.ProductDetails(
                                                        productId = it.id
                                                    )
                                                )
                                            })
                                    }

                                }
                            }
                        }

                        // Sticky footer
                        BottomStickyButtons(
                            quantity = quantity.toString(),
                            isLoading = false,
                            onIncreaseHandler = {
                                if (quantity < 10) quantity = quantity + 1
                            },
                            onDecreaseHandler = {
                                if (quantity > 1) quantity = quantity - 1
                            },
                            onAddToCart = {
                                onAddToCartClickHandler(productData.data.id, quantity)
                            }
                        )
                    }
                }

            }
        }

    }
}

@Composable
fun BottomStickyButtons(
    quantity: String,
    isLoading: Boolean,
    onIncreaseHandler: () -> Unit,
    onDecreaseHandler: () -> Unit,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedIconButton(onClick = onDecreaseHandler, shape = RoundedCornerShape(10.dp)) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Decrease Quantity",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            CustomText(quantity, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedIconButton(onClick = onIncreaseHandler, shape = RoundedCornerShape(10.dp)) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increase Quantity",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        LoaderButton(
            onClickBtn = onAddToCart,
            isLoading = isLoading,
        ) { CustomText("Add to cart") }
    }
}




//@Preview(name = "Product details preview", showBackground = true)
//@Composable
//fun ProductDetailsPreview() {
//    OutlinedIconButton (onClick = {}) {
//        Icon(
//            Icons.Outlined.Person,
//            contentDescription = "Person Icon",
//            tint = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.size(16.dp)
//        )
//    }
//}