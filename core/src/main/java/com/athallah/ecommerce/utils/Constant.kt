package com.athallah.ecommerce.utils

object Constant {
    const val API_BASE_URL = "http://192.168.0.10:5000/"
    const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"

    val PRELOGIN_ENDPOINT = listOf(
        "${API_BASE_URL}login",
        "${API_BASE_URL}register",
        "${API_BASE_URL}refresh"
    )
}
