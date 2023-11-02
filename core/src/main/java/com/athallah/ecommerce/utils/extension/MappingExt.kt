package com.athallah.ecommerce.utils.extension

import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.datasource.api.response.AuthDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
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

