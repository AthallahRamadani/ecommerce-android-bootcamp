package com.athallah.ecommerce.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.ProductVariantItem
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponseData
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.fragment.checkout.CheckoutFragment
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.fragment.detail.DetailViewModel
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toDetailProduct
import com.athallah.ecommerce.utils.extension.toWishlist
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class DetailViewModelTest {

    private val dummyProductsDetailResponse = ProductsDetailResponse(
        code = 200,
        message = "OK",
        data = ProductsDetailResponseData(
            productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 24499000,
            image = arrayListOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/0cc3d06c-b09d-4294-8c3f-1c37e60631a6.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/33a06657-9f88-4108-8676-7adafaa94921.jpg"
            ),
            brand = "Asus",
            description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\n\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\nGraphics Memory : 6GB GDDR6\nDiscrete/Optimus : MUX Switch + Optimus\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
            store = "AsusStore",
            sale = 12,
            stock = 2,
            totalRating = 7,
            totalReview = 5,
            totalSatisfaction = 100,
            productRating = 5.0F,
            productVariant = arrayListOf(
                ProductVariantItem(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ),
                ProductVariantItem(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                ),
            )
        )
    )


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var detailViewModel: DetailViewModel
    private val storeRepository: StoreRepository = mock()
    private val wishlistRepository: WishlistRepository = mock()
    private val cartRepository: CartRepository = mock()
    private val savedStateHandle: SavedStateHandle =
        SavedStateHandle(mapOf(DetailFragment.BUNDLE_PRODUCT_ID_KEY to "productId"))

    @Before
    fun setUp() {
        whenever(storeRepository.detailProducts("productId")).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(dummyProductsDetailResponse.data!!.toDetailProduct())
            )
        )
        detailViewModel =
            DetailViewModel(storeRepository, wishlistRepository, cartRepository, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getDetailProduct() = runTest {
        whenever(storeRepository.detailProducts("productId")).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(dummyProductsDetailResponse.data!!.toDetailProduct())
            )
        )
        detailViewModel.getDetailProduct()
        advanceUntilIdle()

        assertEquals(
            ResultState.Success(dummyProductsDetailResponse.data!!.toDetailProduct()),
            detailViewModel.detailProductState.value
        )
    }

    @Test
    fun checkExistWishlist() = runTest {
        whenever(wishlistRepository.checkExistWishlistData("productId")).thenReturn(true)
        val actualData = detailViewModel.checkExistWishlist()
        assertTrue { actualData }
    }

    @Test
    fun insertWishlist() = runTest {
        val detailProduct = dummyProductsDetailResponse.data!!.toDetailProduct()
        val productVariant = detailProduct.productVariant[0]

        detailViewModel.detailProduct = detailProduct
        detailViewModel.productVariant = productVariant

        detailViewModel.insertWishlist()

        verify(wishlistRepository).insertWishlist(detailProduct.toWishlist(productVariant))
    }

    @Test
    fun deleteWishlist() = runTest {
        val detailProduct = dummyProductsDetailResponse.data!!.toDetailProduct()
        val productVariant = detailProduct.productVariant[0]

        detailViewModel.detailProduct = detailProduct
        detailViewModel.productVariant = productVariant

        detailViewModel.deleteWishlist()

        verify(wishlistRepository).deleteWishlist(detailProduct.toWishlist(productVariant))
    }

    @Test
    fun insertCart() = runTest {
        val detailProduct = dummyProductsDetailResponse.data!!.toDetailProduct()
        val productVariant = detailProduct.productVariant[0]
        val cart = detailProduct.toCart(productVariant)
        detailViewModel.detailProduct = detailProduct
        detailViewModel.productVariant = productVariant

        whenever(cartRepository.isStockReady(cart)).thenReturn(true)

        detailViewModel.insertCart()

        verify(cartRepository).insertCart(cart)
    }
}