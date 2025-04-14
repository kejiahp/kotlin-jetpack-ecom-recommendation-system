package com.example.mobile.product_cart_order.cart

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.core.BackButtonWithTitle
import com.example.mobile.core.CustomText
import com.example.mobile.core.ErrorScreen
import com.example.mobile.core.Loader
import com.example.mobile.core.LoaderButton
import com.example.mobile.core.LoaderScreen
import com.example.mobile.core.StickyTopNavbar
import com.example.mobile.core.StyledOutlinedTextField
import com.example.mobile.core.navigation.NavRoutes
import com.example.mobile.product_cart_order.entity.CartItemPopulatedData
import com.example.mobile.product_cart_order.preference.ProdCartOrderSharedPreferenceViewModel

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = hiltViewModel()) {
    val toastCtx = LocalContext.current
    val scrollState = rememberScrollState()
    val prodCartOrderSharedPreferenceViewModel: ProdCartOrderSharedPreferenceViewModel =
        hiltViewModel()
    val userCartQueryState by cartViewModel.userCartQueryState.collectAsState()
    val removeFromCartQueryState by cartViewModel.removeFromCartQueryState.collectAsState()
    val emptyCartQueryState by cartViewModel.emptyCartQueryState.collectAsState()
    val addToCartQueryState by cartViewModel.addToCartQueryState.collectAsState()
    val checkoutQueryState by cartViewModel.checkoutQueryState.collectAsState()
    val receiptFormState by cartViewModel.receiptFormState.collectAsState()

    LaunchedEffect(addToCartQueryState) {
        if (addToCartQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, addToCartQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (addToCartQueryState.data != null) {
            cartViewModel.getUserCart()
        }
    }

    LaunchedEffect(checkoutQueryState) {
        if (checkoutQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, checkoutQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (checkoutQueryState.data != null) {
            Toast.makeText(
                toastCtx, checkoutQueryState.data!!.message, Toast.LENGTH_LONG
            ).show()

            cartViewModel.getUserCart()

            navController.navigate(NavRoutes.OrderScreen)
        }
    }

    LaunchedEffect(emptyCartQueryState) {
        if (emptyCartQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, emptyCartQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (emptyCartQueryState.data != null) {
            cartViewModel.getUserCart()
        }
    }

    LaunchedEffect(removeFromCartQueryState) {
        if (removeFromCartQueryState.errorMsg.isNotEmpty()) {
            Toast.makeText(
                toastCtx, removeFromCartQueryState.errorMsg, Toast.LENGTH_LONG
            ).show()
        }
        if (removeFromCartQueryState.data != null) {
            cartViewModel.getUserCart()
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
                    title = "Cart",
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

                when {

                    userCartQueryState.isLoading -> LoaderScreen()

                    userCartQueryState.errorMsg.isNotEmpty() -> ErrorScreen(userCartQueryState.errorMsg)

                    userCartQueryState.data != null -> {
                        userCartQueryState.data?.let { userCartData ->
                            if (userCartData.data.cartItems.isNotEmpty()) {
                                userCartData.data.cartItems.forEach { cartItem ->
                                    CartItem(
                                        cartItem = cartItem,
                                        onNavigateHandler = { id ->
                                            prodCartOrderSharedPreferenceViewModel.addToRecentlyViewed(
                                                id
                                            )

                                            navController.navigate(
                                                NavRoutes.ProductDetails(
                                                    productId = id
                                                )
                                            )
                                        },
                                        onQtyIncrease = { id, qty ->
                                            if (!addToCartQueryState.isLoading) {
                                                if (qty < 100) {
                                                    cartViewModel.addToCartHandler(id, 1, "ADD")
                                                }
                                            }
                                        },
                                        onQtyDecrease = { id, qty ->
                                            if (!addToCartQueryState.isLoading) {
                                                if (qty > 1) {
                                                    cartViewModel.addToCartHandler(id, 1, "REMOVE")
                                                }
                                            }
                                        },
                                        onRemoveFromCart = { id ->
                                            if (!removeFromCartQueryState.isLoading) {
                                                cartViewModel.removeItemFromCart(id)
                                            }
                                        }
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CustomText(
                                        "Oops...This cart is empty, add some items to it.",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }

                            if (userCartData.data.cartItems.isNotEmpty()) {
                                TextButton(onClick = { if (!emptyCartQueryState.isLoading) cartViewModel.emptyCart() }) {
                                    if (emptyCartQueryState.isLoading) {
                                        Loader()
                                    } else {
                                        CustomText("Empty Cart")
                                    }
                                }
                            }
                        }
                    }

                }
            }

            CustomText(
                "Your receipt will be sent to the email provided.",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Medium
            )
            CustomText(
                "If you can't find the receipt check your spam.",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(0.6f)) {
                    // Email Input Field
                    StyledOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = receiptFormState.email,
                        enabled = userCartQueryState.data != null && userCartQueryState.data!!.data.cartItems.isNotEmpty(),
                        textFieldError = receiptFormState.emailError,
                        imgVec = Icons.Default.Email,
                        keyboardOpt = KeyboardOptions(keyboardType = KeyboardType.Email),
                        onChange = cartViewModel::onEmailChangeHandler,
                        label = { CustomText("Email") },
                        placeholder = { CustomText("Enter your Email", fontSize = 12.sp) },
                        emptyFieldHandler = {
                            cartViewModel.onEmailChangeHandler("")
                        })
                }

                Spacer(modifier = Modifier.width(10.dp))

                LoaderButton(
                    enabled = userCartQueryState.data != null && userCartQueryState.data!!.data.cartItems.isNotEmpty(),
                    isLoading = checkoutQueryState.isLoading,
                    onClickBtn = cartViewModel::onCheckoutHandler,
                    modifier = Modifier
                        .height(50.dp)
                        .weight(0.4f)
                ) {
                    CustomText("Checkout")
                }

            }
        }
    }
}

@Composable
fun CartItem(
    cartItem: CartItemPopulatedData,
    onNavigateHandler: (String) -> Unit,
    onQtyIncrease: (String, Int) -> Unit,
    onQtyDecrease: (String, Int) -> Unit,
    onRemoveFromCart: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(0.6f)
                .clickable {
                    onNavigateHandler(cartItem.productId.id)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.productId.imageUrl,
                contentDescription = cartItem.productId.productName,
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
                    cartItem.productId.productName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                CustomText(
                    "Quantity: ${cartItem.quantity}",
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
                val itemTotalPrice = cartItem.productId.sellingPrice.toFloat() * cartItem.quantity
                CustomText("₦ $itemTotalPrice", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }
        }

        Column(modifier = Modifier.weight(0.2f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onRemoveFromCart(cartItem.productId.id) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Remove From Cart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedIconButton(
                    onClick = { onQtyDecrease(cartItem.productId.id, cartItem.quantity) },
                    modifier = Modifier.size(30.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Decrease Quantity",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                CustomText("${cartItem.quantity}", fontSize = 14.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedIconButton(
                    onClick = { onQtyIncrease(cartItem.productId.id, cartItem.quantity) },
                    modifier = Modifier.size(30.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Increase Quantity",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Preview(name = "cart screen preview", showBackground = true)
@Composable
fun PreviewSomeStuff() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}
