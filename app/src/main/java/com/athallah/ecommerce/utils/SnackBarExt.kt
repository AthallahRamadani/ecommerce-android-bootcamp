package com.athallah.ecommerce.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Number.toCurrencyFormat(lang: String = "in", country: String = "ID"): String {
    val localId = Locale(lang, country)
    val formatter = NumberFormat.getCurrencyInstance(localId)
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}
