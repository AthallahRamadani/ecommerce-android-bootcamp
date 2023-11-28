package com.athallah.ecommerce.data.datasource.firebase

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebaseSubscribe(
    private val firebaseMessaging: FirebaseMessaging
) {

    fun subscribe() {
        firebaseMessaging.subscribeToTopic("promo")
    }
    fun unsubscribe() {
        firebaseMessaging.unsubscribeFromTopic("promo")
    }

    suspend fun firebaseToken(): String {
        return firebaseMessaging.token.await()
    }


}