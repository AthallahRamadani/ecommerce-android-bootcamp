package com.athallah.ecommerce.utils.extension

import com.athallah.ecommerce.data.datasource.api.model.DetailProduct
import com.athallah.ecommerce.data.datasource.api.model.Product
import com.athallah.ecommerce.data.datasource.api.model.Review
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.datasource.api.model.Wishlist
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductVariantItem
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponseData
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponseItem
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponseItem
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
        store,
        sale,
        stock,
        productRating,
        productVariant
    )

fun Wishlist.toWishlistEntity(): WishlistEntity =
    WishlistEntity(
        productId,
        productName,
        productPrice,
        image,
        store,
        sale,
        stock,
        productRating,
        productVariant
    )



