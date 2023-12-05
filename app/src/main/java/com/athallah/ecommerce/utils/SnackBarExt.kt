package com.athallah.ecommerce.utils

import android.view.View
import com.athallah.ecommerce.data.datasource.api.response.ErrorResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.HttpException
import java.text.NumberFormat
import java.util.Locale

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun getApiErrorMessage(e: Throwable): String? {
    var message = e.message
    if (e is HttpException) {
        val errorResponse =
            Gson().fromJson(
                e.response()?.errorBody()?.string(),
                ErrorResponse::class.java
            ) ?: ErrorResponse()
        errorResponse.message?.let { message = it }
    }
    return message
}

fun Number.toCurrencyFormat(lang: String = "in", country: String = "ID"): String {
    val localId = Locale(lang, country)
    val formatter = NumberFormat.getCurrencyInstance(localId)
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}
