package com.example.mobile.product_cart_order.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.core.CustomText
import com.example.mobile.R
import com.example.mobile.product_cart_order.entity.ProductListingDetailsData

@Composable
fun ProductCard(
    name: String,
    sellingPrice: String,
    originalPrice: String,
    rating: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 0.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
            .wrapContentWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Product image
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.placeholder_image),
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Product name
            Row(
                modifier = Modifier.width(150.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomText(
                    modifier = Modifier.width(100.dp),
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    CustomText(text = rating, fontSize = 12.sp)
                }

            }

            Spacer(modifier = Modifier.height(4.dp))

            // Product price
            CustomText(
                text = "₦ $sellingPrice",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            CustomText(
                text = "₦ $originalPrice",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Composable
fun ProductGrid(
    title: String,
    products: List<ProductListingDetailsData>,
    onProductClick: (ProductListingDetailsData) -> Unit,
    headerContent: @Composable (LazyGridItemScope.() -> Unit),
) {
    Box(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                headerContent()
            }

            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                CustomText(title, fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            if (products.isEmpty()) {
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            "Oops...This category is empty, there are no products to see here",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            } else {
                items(products) { product ->
                    ProductCard(
                        name = product.productName,
                        imageUrl = product.imageUrl,
                        sellingPrice = product.sellingPrice,
                        originalPrice = product.productPrice,
                        rating = product.avgRating.toString(),
                        onClick = { onProductClick(product) }
                    )
                }
            }

        }
    }
}

@Composable
fun ProductListing(
    title: String,
    products: List<ProductListingDetailsData>,
    onProductClick: (ProductListingDetailsData) -> Unit
) {
    if (products.isNotEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)) {
            CustomText(title, fontSize = 20.sp, fontWeight = FontWeight.Medium)

            LazyRow {
                items(products) { item ->
                    ProductCard(
                        name = item.productName,
                        imageUrl = item.imageUrl,
                        sellingPrice = item.sellingPrice,
                        originalPrice = item.productPrice,
                        rating = item.avgRating.toString(),
                        onClick = { onProductClick(item) }
                    )
                }
            }
        }
    }
}