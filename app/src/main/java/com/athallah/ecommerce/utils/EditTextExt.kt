package com.athallah.ecommerce.utils

import com.google.android.material.textfield.TextInputEditText

val TextInputEditText.textTrimmed get() = text?.toString().orEmpty().trim()