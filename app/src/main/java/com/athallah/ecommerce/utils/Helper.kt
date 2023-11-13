package com.athallah.ecommerce.utils

import android.content.Context
import android.util.TypedValue

object Helper {
    fun getColorTheme(context: Context, colorName: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorName, typedValue, true)
        return typedValue.data
    }
}