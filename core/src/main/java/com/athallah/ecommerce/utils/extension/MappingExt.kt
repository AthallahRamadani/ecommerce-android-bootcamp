package com.athallah.ecommerce.utils.extension

import android.view.View
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.datasource.api.response.AuthDataResponse
import com.google.android.material.snackbar.Snackbar
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun AuthDataResponse.toUser(): User =
    User(accessToken = accessToken, refreshToken = refreshToken, expiresAt = expiresAt)

fun File.toMultipartBody(field: String): MultipartBody.Part {
    val requestImageFile = this.asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData(
        field,
        this.name,
        requestImageFile
    )
}

fun String.toBearerToken(): String = "Bearer $this"

