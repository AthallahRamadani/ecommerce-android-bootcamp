package com.athallah.ecommerce.data.source.remote.request

import com.athallah.ecommerce.utils.Constant


data class AuthRequest(
    val email: String,
    val password: String,
    val firebaseToken: String = Constant.FIREBASE_TOKEN
)



