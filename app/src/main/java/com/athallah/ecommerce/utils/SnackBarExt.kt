package com.athallah.ecommerce.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String) {
    Snackbar.make(context, this, message, Snackbar.LENGTH_LONG)
        .show()
}