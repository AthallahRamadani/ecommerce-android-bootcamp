package com.athallah.ecommerce.fragment.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.fragment.checkout.CheckoutFragment
import com.athallah.ecommerce.fragment.review.ReviewFragmentCompose
import com.athallah.ecommerce.utils.compose.AppTheme
import com.athallah.ecommerce.utils.compose.poppinsFamily
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.util.Locale

class DetailFragmentCompose : Fragment() {

    private val viewModel: DetailViewModelCompose by viewModel()
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    DetailFragmentScreen(
                        firebaseAnalytics = firebaseAnalytics,
                        viewModel = viewModel,
                        onBackPressed = {
                            findNavController().navigateUp()
                        },
                        onClickBuyNow = {
                            val product = viewModel.detailComposeState.value.detailProduct
                            val variant = viewModel.detailComposeState.value.productVariant
                            val data = product!!.toCart(variant!!)
                            val bundle = Bundle().apply {
                                putParcelableArrayList(CheckoutFragment.ARG_DATA, arrayListOf(data))
                            }
                            findNavController().navigate(
                                R.id.action_detailFragmentCompose_to_checkoutFragment,
                                bundle
                            )
                        },
                        onErrorAction = { viewModel.getDetailProduct() },
                        onReviewClicked = {
                            findNavController().navigate(
                                R.id.action_detailFragmentCompose_to_reviewFragmentCompose,
                                bundleOf(ReviewFragmentCompose.BUNDLE_PRODUCT_ID_KEY to viewModel.productId)
                            )
                        },
                    )
                }
            }
        }
    }

    companion object {
        const val BUNDLE_PRODUCT_ID_KEY = "product_id"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFragmentScreen(
    firebaseAnalytics: FirebaseAnalytics,
    viewModel: DetailViewModelCompose,
    onBackPressed: () -> Unit,
    onClickBuyNow: () -> Unit,
    onErrorAction: () -> Unit,
    onReviewClicked: () -> Unit
) {
    val context = LocalContext.current

    val state = viewModel.detailComposeState.collectAsState()
    val resultState = state.value.resultState

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState()

    LaunchedEffect(dismissSnackbarState.currentValue) {
        if (dismissSnackbarState.currentValue != DismissValue.Default) {
            snackbarHostState.currentSnackbarData?.dismiss()
            delay(300L)
            dismissSnackbarState.snapTo(DismissValue.Default)
        }
    }

    Scaffold(
        topBar = { DetailAppBar(onBackPressed) },
        bottomBar = {
            if (resultState is ResultState.Success) {
                BottomButton(
                    onClickBuyNow = { onClickBuyNow() },
                    onClickAddCart = {
                        if (viewModel.insertCart()) {
                            sendLogWishlistOrCart(firebaseAnalytics, Event.ADD_TO_CART, state.value)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.cart_insert_successful)
                                )
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.stock_is_unavailable)
                                )
                            }
                        }
                    },
                )
            }
        },
        snackbarHost = {
            SwipeToDismiss(
                state = dismissSnackbarState,
                background = {},
                dismissContent = {
                    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.imePadding())
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (resultState) {
                is ResultState.Loading -> Loading(Modifier.fillMaxSize())
                is ResultState.Success -> DetailContent(
                    dataState = state.value,
                    onSharedClick = {
                        actionShare(context, state.value)
                    },
                    onWishlistClick = {
                        if (state.value.isWishlist) {
                            viewModel.deleteWishlist()
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.wishlist_remove_successful)
                                )
                            }
                        } else {
                            viewModel.insertWishlist()
                            sendLogWishlistOrCart(
                                firebaseAnalytics,
                                Event.ADD_TO_WISHLIST,
                                state.value
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.wishlist_insert_successful)
                                )
                            }
                        }
                    },
                    onVariantClicked = { viewModel.updateVariant(it) },
                    onReviewClicked = { onReviewClicked() },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )

                is ResultState.Error -> Error(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    resultState.e,
                    onErrorAction
                )

                else -> {}
            }
        }
    }
}

@Composable
fun DetailContent(
    dataState: DetailComposeState,
    onSharedClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onVariantClicked: (DetailProduct.ProductVariant) -> Unit,
    onReviewClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ImageBanner(dataState.detailProduct!!.image)
        TitleSection(
            dataState.detailProduct,
            dataState.productVariant!!,
            dataState.isWishlist,
            onSharedClick,
            onWishlistClick,
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Divider()
        VariantSection(
            dataState.detailProduct,
            dataState.productVariant,
            onVariantClicked,
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Divider()
        DescriptionSection(
            dataState.detailProduct,
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Divider()
        ReviewSection(
            dataState.detailProduct,
            onReviewClicked,
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun TitleSection(
    product: DetailProduct,
    variant: DetailProduct.ProductVariant,
    isWishlist: Boolean,
    onSharedClick: () -> Unit,
    onWishlistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val price = product.productPrice + variant.variantPrice
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = price.toCurrencyFormat(),
                fontSize = 20.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onSharedClick() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_share_24),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = { onWishlistClick() },
                modifier = Modifier.size(24.dp)
            ) {
                if (isWishlist) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_favorite_purple_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.productName,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.total_sales, product.sale),
                fontSize = 12.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                Modifier.border(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(4.dp),
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp, 2.dp, 8.dp, 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(
                            id = R.string.detail_rating_and_total_rating,
                            product.productRating,
                            product.totalRating
                        ),
                        fontSize = 12.sp,
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Normal,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VariantSection(
    product: DetailProduct,
    variant: DetailProduct.ProductVariant,
    onVariantClicked: (DetailProduct.ProductVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.choose_variant),
            fontSize = 16.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val variantList = product.productVariant
            repeat(variantList.size) { index ->
                val isSelected = variantList[index] == variant
                FilterChip(
                    selected = isSelected,
                    onClick = { onVariantClicked(variantList[index]) },
                    label = {
                        Text(
                            text = variantList[index].variantName,
                            style = TextStyle(
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                ),
                            ),
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DescriptionSection(product: DetailProduct, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.product_description),
            fontSize = 16.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.description,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
    }
}

@Composable
fun ReviewSection(
    product: DetailProduct,
    onMoreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.buyer_review),
                fontSize = 16.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
            )
            TextButton(
                onClick = { onMoreClicked() },
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                modifier = Modifier.height(22.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.see_all),
                    fontSize = 12.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = String.format(Locale.getDefault(), "%.1f", product.productRating),
                fontSize = 20.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
            )
            Text(
                text = stringResource(id = R.string._5_0),
                fontSize = 10.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                Text(
                    text = stringResource(
                        id = R.string.text_satisfaction,
                        product.totalSatisfaction
                    ),
                    fontSize = 12.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
                Text(
                    text = stringResource(
                        id = R.string.rating_and_review_total,
                        product.totalRating,
                        product.totalReview
                    ),
                    fontSize = 12.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageBanner(dataImage: List<String>) {
    val pagerState = rememberPagerState(pageCount = {
        dataImage.size
    })
    Box {
        HorizontalPager(state = pagerState) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dataImage[it])
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.product_image_placeholder),
                error = painterResource(id = R.drawable.product_image_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .height(309.dp)
                    .fillMaxWidth()
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }
                if (pagerState.pageCount > 1){
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

private fun actionShare(context: Context, state: DetailComposeState) {
    val sendIntent: Intent = Intent().apply {
        val text = """
                Name : ${state.detailProduct?.productName}
                Price : ${state.detailProduct?.productPrice?.toCurrencyFormat()}
                Link : https://ecommerce.athallah.com/products/${state.productId}
        """.trimIndent()
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun Throwable.getErrorTitle(context: Context): String {
    return when (this) {
        is HttpException -> if (response()?.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            context.getString(R.string.empty_title)
        } else {
            code().toString()
        }

        is IOException -> context.getString(R.string.connection_title)
        else -> context.getString(R.string.error)
    }
}

fun Throwable.getErrorSubtitle(context: Context): String {
    return when (this) {
        is HttpException -> if (response()?.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            context.getString(R.string.empty_subtitle)
        } else {
            response()?.message().toString()
        }

        is IOException -> context.getString(R.string.connection_subtitle)
        else -> message.toString()
    }
}

@Composable
fun Error(modifier: Modifier = Modifier, error: Throwable, onErrorAction: () -> Unit) {
    val title = error.getErrorTitle(LocalContext.current)
    val subtitle = error.getErrorSubtitle(LocalContext.current)
    val action = stringResource(id = R.string.refresh)
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.smartphone),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 32.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 16.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onErrorAction() }) {
            Text(
                text = action,
                fontSize = 14.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
            )
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

private fun sendLogWishlistOrCart(
    firebaseAnalytics: FirebaseAnalytics,
    event: String,
    state: DetailComposeState
) {
    firebaseAnalytics.logEvent(event) {
        val bundleProduct = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, state.detailProduct!!.productId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, state.detailProduct.productName)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, state.detailProduct.brand)
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, state.productVariant!!.variantName)
        }
        param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
    }
}

@Composable
fun BottomButton(
    onClickBuyNow: () -> Unit,
    onClickAddCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Divider()
        Row(
            Modifier
                .padding(16.dp, 8.dp, 16.dp, 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { onClickBuyNow() }
            ) {
                Text(
                    text = stringResource(id = R.string.buy_now),
                    fontSize = 14.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { onClickAddCart() }
            ) {
                Text(
                    text = stringResource(id = R.string.cart_plus),
                    fontSize = 14.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAppBar(onBackPressed: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.product_detail),
                    fontSize = 22.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = null
                    )
                }
            }
        )
        Divider()
    }
}
