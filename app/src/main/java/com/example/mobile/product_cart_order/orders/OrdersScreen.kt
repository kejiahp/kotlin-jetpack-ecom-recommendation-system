package com.example.mobile.product_cart_order.orders

import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.CustomButton
import com.example.mobile.core.CustomText
import com.example.mobile.core.ErrorScreen
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.StickyTopNavbar
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.core.utilites.CoreUtils
import com.example.mobile.product_cart_order.entity.CartItemPopulatedData
import com.example.mobile.product_cart_order.preference.ProdCartOrderSharedPreferenceViewModel

@Composable
fun OrderScreen(
    navController: NavController,
    prodCartOrderSharedPreferenceViewModel: ProdCartOrderSharedPreferenceViewModel = hiltViewModel(),
    ordersViewModel: OrdersViewModel = hiltViewModel()
) {
    val toastCtx = LocalContext.current
    val scrollState = rememberScrollState()
    val usersOrdersQueryState by ordersViewModel.usersOrdersQueryState.collectAsState()
    val rateProductQueryState by ordersViewModel.rateProductQueryState.collectAsState()


    LaunchedEffect(rateProductQueryState) {
        CoreUtils.printDebugger("OrderScreen", rateProductQueryState)
        if (rateProductQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, rateProductQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (rateProductQueryState.data != null) {
            Toast.makeText(
                toastCtx, rateProductQueryState.data!!.message, Toast.LENGTH_LONG
            ).show()

            ordersViewModel.getAllUserOrder()
        }
    }

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
                BackButtonWithTitle(
                    title = "Orders",
                    titleModifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    titleOverflow = TextOverflow.Ellipsis
                ) { navController.navigate(NavRoutes.ProductHomeScreen) }
            }


            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .weight(1f),
            ) {

                when {

                    usersOrdersQueryState.isLoading -> LoaderScreen()

                    usersOrdersQueryState.errorMsg.isNotEmpty() -> ErrorScreen(usersOrdersQueryState.errorMsg)

                    usersOrdersQueryState.data != null -> {
                        usersOrdersQueryState.data?.let { orderData ->
                            if (orderData.data.isNotEmpty()) {
                                orderData.data.forEach { order ->

                                    Column {
                                        CustomText(
                                            "Order No: ${order.orderNo}",
                                            fontWeight = FontWeight.Medium
                                        )
                                        CustomText(
                                            "Order Total: ${order.orderTotal}",
                                            fontWeight = FontWeight.Medium
                                        )
                                        CustomText(
                                            "Order Status: ${order.orderStatus}",
                                            fontWeight = FontWeight.Medium
                                        )

                                        order.orderItem.forEach { item ->
                                            OrderItem(
                                                orderItem = item,
                                                onRateClickHandler = { id, ratingNumber ->
                                                    ordersViewModel.onRatingHandler(
                                                        productId = id,
                                                        ratingNumber = ratingNumber
                                                    )
                                                },
                                                onBuyAgainClickHandler = { id ->
                                                    prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(id)

                                                    navController.navigate(
                                                        NavRoutes.ProductDetails(
                                                            productId = id
                                                        )
                                                    )
                                                })
                                        }

                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(vertical = 10.dp)
                                        )

                                    }

                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CustomText(
                                        "You haven't made any orders yet",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun OrderItem(
    orderItem: CartItemPopulatedData,
    onBuyAgainClickHandler: (String) -> Unit,
    onRateClickHandler: (String, Int) -> Unit
) {
    var showRatingSection by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(0.6f), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = orderItem.productId.imageUrl,
                    contentDescription = orderItem.productId.productName,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_image),
                    error = painterResource(id = R.drawable.placeholder_image),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    CustomText(
                        orderItem.productId.productName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    CustomText(
                        "Quantity: ${orderItem.quantity}",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                    val itemTotalPrice =
                        orderItem.productId.sellingPrice.toFloat() * orderItem.quantity
                    CustomText(
                        "₦ $itemTotalPrice",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }

            Column(modifier = Modifier.weight(0.3f), horizontalAlignment = Alignment.End) {
                TextButton(onClick = { onBuyAgainClickHandler(orderItem.productId.id) }) {
                    CustomText("Buy Again")
                }

                CustomButton(
                    enabled = !orderItem.productId.isRated,
                    onClickBtn = { showRatingSection = true }) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    if (!orderItem.productId.isRated) {
                        CustomText("Rate")
                    } else {
                        CustomText(orderItem.productId.ratingGiven.toString())
                    }
                }

            }
        }

        if (showRatingSection) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(1, 2, 3, 4, 5).forEach { number ->
                    CustomButton(onClickBtn = {
                        onRateClickHandler(
                            orderItem.productId.id,
                            number
                        )
                    }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        CustomText(number.toString(), fontSize = 12.sp)
                    }
                }
            }
        }
    }


}