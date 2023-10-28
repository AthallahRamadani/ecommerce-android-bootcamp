package com.athallah.ecommerce.data.model

data class User(
    val userName: String? = null,
    val userImage: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: Int? = null
)
