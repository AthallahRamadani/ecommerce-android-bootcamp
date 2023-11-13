package com.athallah.ecommerce.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.textfield.TextInputEditText

val TextInputEditText.textTrimmed get() = text?.toString().orEmpty().trim()

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}