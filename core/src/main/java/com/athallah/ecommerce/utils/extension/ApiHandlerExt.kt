package com.athallah.ecommerce.utils.extension

import com.athallah.ecommerce.data.datasource.api.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

fun Throwable.getErrorMessage(): String? {
    var message = this.message
    if (this is HttpException) {
        val errorResponse =
            Gson().fromJson(
                this.response()?.errorBody()?.string(),
                ErrorResponse::class.java
            ) ?: ErrorResponse()
        errorResponse.message?.let { message = it }
    }
    return message
}