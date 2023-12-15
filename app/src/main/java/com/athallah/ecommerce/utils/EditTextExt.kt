package com.athallah.ecommerce.utils

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.textfield.TextInputEditText

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else ->
        @Suppress("DEPRECATION")
        getParcelable(key)
}
