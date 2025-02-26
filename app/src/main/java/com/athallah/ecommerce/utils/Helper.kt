package com.athallah.ecommerce.utils

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object Helper {
    fun getColorTheme(context: Context, colorName: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorName, typedValue, true)
        return typedValue.data
    }

    fun setAppTheme(context: Context, isDarkTheme: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiMode = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            uiMode.setApplicationNightMode(
                if (isDarkTheme) {
                    UiModeManager.MODE_NIGHT_YES
                } else {
                    UiModeManager.MODE_NIGHT_NO
                }
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }

    fun setAppLanguage(appLanguage: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(appLanguage))
    }
}
