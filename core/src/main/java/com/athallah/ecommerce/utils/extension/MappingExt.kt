package com.athallah.ecommerce.utils.extension

import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.data.datasource.model.Product
import com.athallah.ecommerce.data.datasource.model.Review
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponseData
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponseDataItem
import com.athallah.ecommerce.data.datasource.api.response.ProductVariantItem
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponseData
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponseItem
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponseItem
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


fun LoginDataResponse.toUser(): User =
    User(
        userName = userName,
        userImage = userImage,
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresAt = expiresAt
    )

fun RegisterDataResponse.toUser(): User =
    User(accessToken = accessToken, refreshToken = refreshToken, expiresAt = expiresAt)

fun ProfileDataResponse.toUser(): User =
    User(userName = userName, userImage = userImage)


fun File.toMultipartBody(field: String): MultipartBody.Part {
    val requestImageFile = this.asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData(
        field,
        this.name,
        requestImageFile
    )
}

fun String.toBearerToken(): String = "Bearer $this"

fun ProductsQuery.toQueryMap(): Map<String, String> {
    val map = HashMap<String, String>()
    search?.let { map.put("search", it) }
    brand?.let { map.put("brand", it) }
    lowest?.let { map.put("lowest", it) }
    highest?.let { map.put("highest", it) }
    sort?.let { map.put("sort", it) }
    limit?.let { map.put("limit", it.toString()) }
    page?.let { map.put("page", it.toString()) }
    return map
}

fun ProductsResponseItem.toProduct(): Product =
    Product(
        productId ?: "",
        productName ?: "",
        productPrice ?: 0,
        image ?: "",
        brand ?: "",
        store ?: "",
        sale ?: 0,
        productRating ?: 0F
    )

fun ProductsDetailResponseData.toDetailProduct(): DetailProduct =
    DetailProduct(
        image ?: emptyList(),
        productId ?: "",
        description ?: "",
        totalRating ?: 0,
        store ?: "",
        productName ?: "",
        totalSatisfaction ?: 0,
        sale ?: 0,
        productVariant?.map { it.toProductVariant() } ?: emptyList(),
        stock ?: 0,
        productRating ?: 0F,
        brand ?: "",
        productPrice ?: 0,
        totalReview ?: 0
    )

private fun ProductVariantItem.toProductVariant(): DetailProduct.ProductVariant =
    DetailProduct.ProductVariant(
        variantPrice ?: 0,
        variantName ?: ""
    )

fun ReviewResponseItem.toReview(): Review =
    Review(
        userImage ?: "",
        userName ?: "",
        userReview ?: "",
        userRating ?: 0
    )

fun WishlistEntity.toWishlist(): Wishlist =
    Wishlist(
        productId,
        productName,
        productPrice,
        image,
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variantName,
        variantPrice
    )

fun Wishlist.toWishlistEntity(): WishlistEntity =
    WishlistEntity(
        productId,
        productName,
        productPrice,
        image,
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variantName,
        variantPrice
    )

fun CartEntity.toCart(): Cart =
    Cart(
        productId,
        productName,
        productPrice,
        image,
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variantName,
        variantPrice,
        quantity,
        isChecked,
    )
fun Cart.toCartEntity(): CartEntity =
    CartEntity(
        productId,
        productName,
        productPrice,
        image,
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variantName,
        variantPrice,
        quantity ?: 1,
        isChecked,
    )

fun DetailProduct.toCart(variant: DetailProduct.ProductVariant): Cart =
    Cart(
        productId,
        productName,
        productPrice,
        image[0],
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variant.variantName,
        variant.variantPrice,
    )

fun Wishlist.toCart(): Cart =
    Cart(
        productId,
        productName,
        productPrice,
        image,
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variantName,
        variantPrice,
    )

fun DetailProduct.toWishlist(variant: DetailProduct.ProductVariant): Wishlist =
    Wishlist(
        productId,
        productName,
        productPrice,
        image[0],
        stock,
        store,
        sale,
        productRating,
        description,
        totalRating,
        totalSatisfaction,
        brand,
        totalReview,
        variant.variantName,
        variant.variantPrice
    )

fun PaymentResponseData.toPayment(): Payment =
    Payment(
        title ?: "",
        item?.map { it.toPaymentItem() } ?: ArrayList()
    )

fun PaymentResponseDataItem.toPaymentItem(): Payment.PaymentItem =
    Payment.PaymentItem(
        image ?: "",
        label ?: "",
        status ?: false
    )


