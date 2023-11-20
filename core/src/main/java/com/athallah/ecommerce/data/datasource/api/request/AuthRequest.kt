package com.athallah.ecommerce.data.datasource.api.request


data class AuthRequest(
    val email: String,
    val password: String,
    val firebaseToken: String
)






